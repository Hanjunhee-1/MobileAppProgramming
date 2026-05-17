package edu.skku.cs.personalproject.Activities.Todo

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import edu.skku.cs.personalproject.APIs.ApiClient
import edu.skku.cs.personalproject.DTOs.Todo.UpdateTodoRequest
import edu.skku.cs.personalproject.Activities.Login.TokenManager
import edu.skku.cs.personalproject.MainActivity
import edu.skku.cs.personalproject.databinding.ActivityTodoEditBinding

import kotlinx.coroutines.launch

class TodoEditActivity :
    AppCompatActivity() {

    private lateinit var binding:
            ActivityTodoEditBinding

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityTodoEditBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        val todoId =
            intent.getIntExtra(
                "todo_id",
                -1
            )

        loadTodo(todoId)

        binding.baseValueEditText.filters =
            arrayOf(

                InputFilter { source,
                              start,
                              end,
                              dest,
                              dstart,
                              dend ->

                    try {

                        val newValue =

                            dest.toString()
                                .substring(
                                    0,
                                    dstart
                                ) +

                                    source.toString()
                                        .substring(
                                            start,
                                            end
                                        ) +

                                    dest.toString()
                                        .substring(
                                            dend
                                        )

                        if (newValue.isEmpty()) {

                            return@InputFilter null
                        }

                        val input =
                            newValue.toInt()

                        if (input in 0..100) {

                            null

                        } else {

                            ""
                        }

                    } catch (e: Exception) {

                        ""
                    }
                }
            )

        binding.saveButton
            .setOnClickListener {

                updateTodo(todoId)
            }
    }

    private fun getSelectedImportance(): String {

        return when (
            binding.importanceRadioGroup.checkedRadioButtonId
        ) {

            binding.radioLowest.id ->
                "LOWEST"

            binding.radioLow.id ->
                "LOW"

            binding.radioMidLow.id ->
                "MID_LOW"

            binding.radioMid.id ->
                "MID"

            binding.radioMidHigh.id ->
                "MID_HIGH"

            binding.radioHigh.id ->
                "HIGH"

            binding.radioHighest.id ->
                "HIGHEST"

            else ->
                ""
        }
    }

    private fun updateTodo(
        todoId: Int
    ) {

        val token =
            TokenManager(this)
                .getToken()

        val baseValueText =
            binding.baseValueEditText
                .text
                .toString()

        if (baseValueText.isBlank()) {

            Toast.makeText(

                this,

                "기본 가치를 입력하세요",

                Toast.LENGTH_SHORT

            ).show()

            return
        }

        val importance =
            getSelectedImportance()

        if (importance.isBlank()) {

            Toast.makeText(

                this,

                "중요도를 선택하세요",

                Toast.LENGTH_SHORT

            ).show()

            return
        }

        val request =
            UpdateTodoRequest(

                binding.nameEditText
                    .text
                    .toString(),

                binding.descriptionEditText
                    .text
                    .toString(),

                baseValueText.toInt(),

                importance,

                binding.completeCheckBox
                    .isChecked
            )

        lifecycleScope.launch {

            try {

                val response =
                    ApiClient
                        .create()
                        .updateTodo(

                            "Bearer $token",

                            todoId,

                            request
                        )

                if (response.is_completed) {

                    Toast.makeText(

                        this@TodoEditActivity,

                        "TODO 완료! +${response.reward_gold} Gold 획득",

                        Toast.LENGTH_LONG

                    ).show()
                }

                val intent =
                    Intent(
                        this@TodoEditActivity,
                        MainActivity::class.java
                    )

                intent.putExtra(
                    "open_fragment",
                    "todos"
                )

                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_SINGLE_TOP

                startActivity(intent)

                finish()

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(

                    this@TodoEditActivity,

                    "수정 실패",

                    Toast.LENGTH_SHORT

                ).show()
            }
        }
    }

    private fun loadTodo(
        todoId: Int
    ) {

        val token =
            TokenManager(this)
                .getToken()

        lifecycleScope.launch {

            try {

                val todo =
                    ApiClient
                        .create()
                        .getTodo(

                            "Bearer $token",

                            todoId
                        )

                binding.nameEditText
                    .setText(todo.name)

                binding.descriptionEditText
                    .setText(todo.description)

                binding.baseValueEditText
                    .setText(
                        todo.base_value
                            .toString()
                    )

                binding.completeCheckBox
                    .isChecked =
                    todo.is_completed

                when (todo.importance) {

                    "LOWEST" ->
                        binding.radioLowest.isChecked = true

                    "LOW" ->
                        binding.radioLow.isChecked = true

                    "MID_LOW" ->
                        binding.radioMidLow.isChecked = true

                    "MID" ->
                        binding.radioMid.isChecked = true

                    "MID_HIGH" ->
                        binding.radioMidHigh.isChecked = true

                    "HIGH" ->
                        binding.radioHigh.isChecked = true

                    "HIGHEST" ->
                        binding.radioHighest.isChecked = true
                }

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }
}