package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String = "",
    val priority: String = "MEDIUM", // LOW, MEDIUM, HIGH
    val dueDate: Long,
    val isCompleted: Boolean = false,
    val moduleName: String = "General"
)
