package edu.skku.cs.personalproject.DTOs.Todo

data class UpdateTodoRequest(
    val name: String,
    val description: String,
    val base_value: Int,
    val importance: String,
    val is_completed: Boolean
)
