package com.example.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiApi
import com.example.data.api.QuizQuestion
import com.example.data.local.StudyDatabase
import com.example.data.model.Exam
import com.example.data.model.Note
import com.example.data.model.Task
import com.example.data.repository.StudyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    object Idle : UiState<Nothing>
    object Loading : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

class StudyViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: StudyRepository

    init {
        val database = StudyDatabase.getDatabase(application)
        repository = StudyRepository(database.studyDao())
    }

    // --- Rooms Data Flows ---
    val allTasks: StateFlow<List<Task>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotes: StateFlow<List<Note>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allExams: StateFlow<List<Exam>> = repository.allExams
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Search Query For Notes ---
    private val _notesSearchQuery = MutableStateFlow("")
    val notesSearchQuery = _notesSearchQuery.asStateFlow()

    private val _selectedFilterModule = MutableStateFlow("All")
    val selectedFilterModule = _selectedFilterModule.asStateFlow()

    val filteredNotes: StateFlow<List<Note>> = combine(
        allNotes,
        _notesSearchQuery,
        _selectedFilterModule
    ) { notes, query, filterModule ->
        notes.filter { note ->
            val matchQuery = note.title.contains(query, ignoreCase = true) ||
                    note.content.contains(query, ignoreCase = true)
            val matchModule = filterModule == "All" || note.moduleName.equals(filterModule, ignoreCase = true)
            matchQuery && matchModule
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Authenticated User State ---
    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn = _isUserLoggedIn.asStateFlow()

    private val _userName = MutableStateFlow("Slimane El Amrani")
    val userName = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow("slimane.amrani@ofppt-edu.ma")
    val userEmail = _userEmail.asStateFlow()

    // --- Themes ---
    private val _isDarkMode = MutableStateFlow(true) // Default dark theme for design appeal
    val isDarkMode = _isDarkMode.asStateFlow()

    // --- AI Assistant Logic States ---
    private val _aiSummaryState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val aiSummaryState = _aiSummaryState.asStateFlow()

    private val _aiExplanationState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val aiExplanationState = _aiExplanationState.asStateFlow()

    private val _aiQuizState = MutableStateFlow<UiState<List<QuizQuestion>>>(UiState.Idle)
    val aiQuizState = _aiQuizState.asStateFlow()

    // --- Quiz Engine ---
    private val _currentQuizIndex = MutableStateFlow(0)
    val currentQuizIndex = _currentQuizIndex.asStateFlow()

    private val _quizScore = MutableStateFlow(0)
    val quizScore = _quizScore.asStateFlow()

    private val _quizSelectedAnswer = MutableStateFlow<Int?>(null)
    val quizSelectedAnswer = _quizSelectedAnswer.asStateFlow()

    private val _quizIsAnswered = MutableStateFlow(false)
    val quizIsAnswered = _quizIsAnswered.asStateFlow()

    // --- Gamifications & Points ---
    private val _xpPoints = MutableStateFlow(120) // Base points to start visually
    val xpPoints = _xpPoints.asStateFlow()

    private val _studyStreak = MutableStateFlow(3) // Base streaks
    val studyStreak = _studyStreak.asStateFlow()

    // --- Navigation Helper Dialogs ---
    private val _isCustomApiKeyDialogShown = MutableStateFlow(false)
    val isCustomApiKeyDialogShown = _isCustomApiKeyDialogShown.asStateFlow()

    // --- CRUD Actions ---

    fun login(name: String, email: String) {
        viewModelScope.launch {
            if (name.isNotBlank() && email.isNotBlank()) {
                _userName.value = name
                _userEmail.value = email
                _isUserLoggedIn.value = true
                addXp(30) // Initial login XP
            }
        }
    }

    fun logout() {
        _isUserLoggedIn.value = false
    }

    fun toggleTheme() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun setSearchQuery(query: String) {
        _notesSearchQuery.value = query
    }

    fun setSelectedFilterModule(module: String) {
        _selectedFilterModule.value = module
    }

    fun addXp(points: Int) {
        _xpPoints.value += points
    }

    // Task triggers Room
    fun addTask(title: String, description: String, priority: String, dueDate: Long, moduleName: String) {
        viewModelScope.launch {
            val task = Task(
                title = title,
                description = description,
                priority = priority,
                dueDate = dueDate,
                isCompleted = false,
                moduleName = moduleName
            )
            repository.insertTask(task)
            addXp(15) // XP for setting custom study challenge
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val updated = task.copy(isCompleted = !task.isCompleted)
            repository.updateTask(updated)
            if (updated.isCompleted) {
                addXp(30) // Double rewarding XP on completing a task
                _studyStreak.value += 1 // Increment study streak
            } else {
                addXp(-30)
                _studyStreak.value = maxOf(0, _studyStreak.value - 1)
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    // Note triggers Room
    fun addNote(title: String, content: String, moduleName: String) {
        viewModelScope.launch {
            val note = Note(
                title = title,
                content = content,
                moduleName = moduleName,
                timestamp = System.currentTimeMillis()
            )
            repository.insertNote(note)
            addXp(10) // XP for note journaling
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    // Exam triggers Room
    fun addExam(moduleName: String, title: String, date: Long, revisionStatus: String) {
        viewModelScope.launch {
            val exam = Exam(
                moduleName = moduleName,
                examTitle = title,
                examDate = date,
                revisionStatus = revisionStatus
            )
            repository.insertExam(exam)
            addXp(20) // Setting focus on exam goals
        }
    }

    fun updateExamStatus(exam: Exam, newStatus: String) {
        viewModelScope.launch {
            val updated = exam.copy(revisionStatus = newStatus)
            repository.updateExam(updated)
            if (newStatus == "REVISED") {
                addXp(40) // Substantial award for full course revision
            }
        }
    }

    fun deleteExam(exam: Exam) {
        viewModelScope.launch {
            repository.deleteExam(exam)
        }
    }


    // --- AI Operations using API ---

    fun summarizeLesson(lessonText: String) {
        if (lessonText.isBlank()) {
            _aiSummaryState.value = UiState.Error("Please enter lesson content to summarize.")
            return
        }

        _aiSummaryState.value = UiState.Loading
        viewModelScope.launch {
            val prompt = """
                Summarize the following lesson content for an OFPPT technical student. 
                Focus on core engineering/coding concepts, practical takeaways, and key terminology.
                Format the summary with elegant markdown, utilizing bullet points and code block markers if applicable.
                
                Content to summarize:
                $lessonText
            """.trimIndent()

            val result = GeminiApi.generateContent(prompt)
            if (result == "API_KEY_ERROR") {
                _aiSummaryState.value = UiState.Error("Gemini API key is not configured in the Secrets Panel. Please enter an active key.")
            } else if (result.startsWith("Error:") || result.startsWith("Network")) {
                _aiSummaryState.value = UiState.Error(result)
            } else {
                _aiSummaryState.value = UiState.Success(result)
                addXp(25) // Earn XP for utilizing smart AI learnings
            }
        }
    }

    fun explainConcept(concept: String) {
        if (concept.isBlank()) {
            _aiExplanationState.value = UiState.Error("Please expand on a technical topic.")
            return
        }

        _aiExplanationState.value = UiState.Loading
        viewModelScope.launch {
            val prompt = """
                Explain the software development or computer science concept of "$concept" to an OFPPT technical student.
                Provide:
                1. A clear high-level metaphor/analogy
                2. Real-world code implementation or structure (if coding-related)
                3. Pro-tips for implementing this in practical programming exams.
                Use styled Markdown, clean structures and clear spacing.
            """.trimIndent()

            val result = GeminiApi.generateContent(prompt)
            if (result == "API_KEY_ERROR") {
                _aiExplanationState.value = UiState.Error("Gemini API key is not configured in the Secrets Panel.")
            } else if (result.startsWith("Error:") || result.startsWith("Network")) {
                _aiExplanationState.value = UiState.Error(result)
            } else {
                _aiExplanationState.value = UiState.Success(result)
                addXp(25)
            }
        }
    }

    fun fetchQuizQuestions(moduleName: String) {
        _aiQuizState.value = UiState.Loading
        _currentQuizIndex.value = 0
        _quizScore.value = 0
        _quizIsAnswered.value = false
        _quizSelectedAnswer.value = null

        viewModelScope.launch {
            val questions = GeminiApi.generateQuiz(moduleName)
            if (questions.isEmpty()) {
                _aiQuizState.value = UiState.Error("Could not retrieve quiz questions. Falling back to local catalog.")
            } else {
                _aiQuizState.value = UiState.Success(questions)
            }
        }
    }

    fun selectQuizAnswer(index: Int, correctIndex: Int) {
        if (_quizIsAnswered.value) return
        _quizSelectedAnswer.value = index
        _quizIsAnswered.value = true

        if (index == correctIndex) {
            _quizScore.value += 1
            addXp(20) // XP reward for correct quiz logic answers
        }
    }

    fun nextQuizQuestion(totalQuestions: Int) {
        if (_currentQuizIndex.value < totalQuestions - 1) {
            _currentQuizIndex.value += 1
            _quizSelectedAnswer.value = null
            _quizIsAnswered.value = false
        }
    }

    fun resetQuizState() {
        _aiQuizState.value = UiState.Idle
        _currentQuizIndex.value = 0
        _quizScore.value = 0
        _quizIsAnswered.value = false
        _quizSelectedAnswer.value = null
    }

    fun resetAiSummaryState() {
        _aiSummaryState.value = UiState.Idle
    }

    fun resetAiExplanationState() {
        _aiExplanationState.value = UiState.Idle
    }
}
