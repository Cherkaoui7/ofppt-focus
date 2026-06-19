package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exams")
data class Exam(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val moduleName: String,
    val examTitle: String = "Final Exam",
    val examDate: Long,
    val revisionStatus: String = "NOT_STARTED" // NOT_STARTED, IN_PROGRESS, REVISED
)
