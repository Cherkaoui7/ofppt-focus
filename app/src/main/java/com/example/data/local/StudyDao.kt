package com.example.data.local

import androidx.room.*
import com.example.data.model.Task
import com.example.data.model.Note
import com.example.data.model.Exam
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyDao {
    // Tasks
    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    fun getAllTasksFlow(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Int)

    // Notes
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotesFlow(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE moduleName = :moduleName ORDER BY timestamp DESC")
    fun getNotesByModuleFlow(moduleName: String): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNoteById(id: Int)

    // Exams
    @Query("SELECT * FROM exams ORDER BY examDate ASC")
    fun getAllExamsFlow(): Flow<List<Exam>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExam(exam: Exam)

    @Update
    suspend fun updateExam(exam: Exam)

    @Delete
    suspend fun deleteExam(exam: Exam)
}
