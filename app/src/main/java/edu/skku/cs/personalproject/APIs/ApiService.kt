package edu.skku.cs.personalproject.APIs

import edu.skku.cs.personalproject.DTOs.History.ValueHistory
import edu.skku.cs.personalproject.DTOs.Login.LoginResponse
import edu.skku.cs.personalproject.DTOs.Todo.CreateTodoRequest
import edu.skku.cs.personalproject.DTOs.Todo.Todo
import edu.skku.cs.personalproject.DTOs.Todo.UpdateTodoRequest
import edu.skku.cs.personalproject.DTOs.User.MyInfo

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // localserver 에 API 요청보냄

    @POST("auth/google")
    suspend fun googleLogin(

        @Body body: Map<String, String>

    ): LoginResponse

    @GET("users/myInfo")
    suspend fun getMyInfo(

        @Header("Authorization")
        token: String

    ): MyInfo

    @GET("todos")
    suspend fun getTodos(

        @Header("Authorization")
        token: String

    ): List<Todo>

    @GET("todos/{todoId}/history")
    suspend fun getHistories(
        @Header("Authorization")
        token: String,

        @Path("todoId")
        todoId: Int

    ): List<ValueHistory>

    @PATCH("todos/{todoId}")
    suspend fun updateTodo(
        @Header("Authorization")
        token: String,

        @Path("todoId")
        todoId: Int,

        @Body request:
        UpdateTodoRequest
    ): Todo

    @GET("todos/{todoId}")
    suspend fun getTodo(
        @Header("Authorization")
        token: String,

        @Path("todoId")
        todoId: Int
    ): Todo

    @DELETE("todos/{todoId}")
    suspend fun deleteTodo(

        @Header("Authorization")
        token: String,

        @Path("todoId")
        todoId: Int
    )

    @POST("todos")
    suspend fun createTodo(

        @Header("Authorization")
        token: String,

        @Body request: CreateTodoRequest

    ): Todo
}