package edu.skku.cs.personalproject.DTOs.User

data class MyInfo(
    val id: Int,
    val name: String,
    val total_gold: Int,
    val todo_count: Int,
    val completed_todo_count: Int
)
