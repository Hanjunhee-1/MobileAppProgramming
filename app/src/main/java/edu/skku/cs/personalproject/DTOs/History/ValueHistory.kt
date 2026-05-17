package edu.skku.cs.personalproject.DTOs.History

data class ValueHistory(
    val id: Int,
    val todo_id: Int,
    val value: Int,
    val created_at: String
)
