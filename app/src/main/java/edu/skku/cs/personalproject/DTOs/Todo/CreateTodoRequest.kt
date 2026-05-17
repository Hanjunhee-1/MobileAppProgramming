package edu.skku.cs.personalproject.DTOs.Todo

data class TodoTime(

    val start: String,

    val end: String
)

data class CreateTodoRequest(

    val name: String,

    val description: String,

    val time: TodoTime,

    val importance: String,

    val base_value: Int
)