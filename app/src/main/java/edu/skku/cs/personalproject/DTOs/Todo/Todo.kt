package edu.skku.cs.personalproject.DTOs.Todo

data class Todo(
    val id: Int,
    val name: String,
    val description: String,
    val importance: String,
    val is_completed: Boolean,
    val current_value: Int,
    val reward_gold: Int,
    val base_value: Int,
    val time: TimeInfo
)

data class TimeInfo(
    val start: String,
    val end: String
)