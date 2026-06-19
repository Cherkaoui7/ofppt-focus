package com.example.data.repository

import com.example.data.local.StudyDao
import com.example.data.model.Task
import com.example.data.model.Note
import com.example.data.model.Exam
import kotlinx.coroutines.flow.Flow

class StudyRepository(private val studyDao: StudyDao) {
    val allTasks: Flow<List<Task>> = studyDao.getAllTasksFlow()
    val allNotes: Flow<List<Note>> = studyDao.getAllNotesFlow()
    val allExams: Flow<List<Exam>> = studyDao.getAllExamsFlow()

    fun getNotesByModule(moduleName: String): Flow<List<Note>> {
        return studyDao.getNotesByModuleFlow(moduleName)
    }

    suspend fun insertTask(task: Task) = studyDao.insertTask(task)
    suspend fun updateTask(task: Task) = studyDao.updateTask(task)
    suspend fun deleteTask(task: Task) = studyDao.deleteTask(task)
    suspend fun deleteTaskById(id: Int) = studyDao.deleteTaskById(id)

    suspend fun insertNote(note: Note) = studyDao.insertNote(note)
    suspend fun updateNote(note: Note) = studyDao.updateNote(note)
    suspend fun deleteNote(note: Note) = studyDao.deleteNote(note)
    suspend fun deleteNoteById(id: Int) = studyDao.deleteNoteById(id)

    suspend fun insertExam(exam: Exam) = studyDao.insertExam(exam)
    suspend fun updateExam(exam: Exam) = studyDao.updateExam(exam)
    suspend fun deleteExam(exam: Exam) = studyDao.deleteExam(exam)
}
