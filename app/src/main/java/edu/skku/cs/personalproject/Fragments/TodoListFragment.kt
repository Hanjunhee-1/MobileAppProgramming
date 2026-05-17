package edu.skku.cs.personalproject.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager

import edu.skku.cs.personalproject.APIs.ApiClient
import edu.skku.cs.personalproject.Adapters.TodoAdapter
import edu.skku.cs.personalproject.DTOs.TokenManager
import edu.skku.cs.personalproject.Activities.Todo.TodoDetailActivity
import edu.skku.cs.personalproject.DTOs.Todo.Todo
import edu.skku.cs.personalproject.DTOs.Todo.UpdateTodoRequest
import edu.skku.cs.personalproject.R
import edu.skku.cs.personalproject.databinding.FragmentTodoListBinding

import kotlinx.coroutines.launch
import java.time.LocalDate

class TodoListFragment : Fragment() {

    private lateinit var adapter:
            TodoAdapter

    private var _binding:
            FragmentTodoListBinding? = null

    private val binding
        get() = _binding!!

    private var showCompleted =
        false

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        _binding =
            FragmentTodoListBinding.inflate(
                inflater,
                container,
                false
            )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(
            view,
            savedInstanceState
        )

        adapter =
            TodoAdapter(

                mutableListOf(),

                // 카드 클릭
                { todo ->

                    val intent =
                        Intent(
                            requireContext(),
                            TodoDetailActivity::class.java
                        )

                    intent.putExtra(
                        "todo_id",
                        todo.id
                    )

                    intent.putExtra(
                        "todo_name",
                        todo.name
                    )

                    intent.putExtra(
                        "description",
                        todo.description
                    )

                    intent.putExtra(
                        "base_value",
                        todo.base_value
                    )

                    intent.putExtra(
                        "is_completed",
                        todo.is_completed
                    )

                    intent.putExtra(
                        "importance",
                        todo.importance
                    )

                    intent.putExtra(
                        "reward_gold",
                        todo.reward_gold
                    )

                    startActivity(intent)
                },

                // 완료 버튼
                { todo ->

                    completeTodo(todo)
                },

                // 삭제 버튼
                { todo ->

                    showDeleteDialog(todo)
                }
            )

        binding.todoRecyclerView
            .layoutManager =
            LinearLayoutManager(
                requireContext()
            )

        binding.todoRecyclerView
            .adapter =
            adapter

        binding.toggleCompletedButton
            .setOnClickListener {

                showCompleted =
                    !showCompleted

                loadTodos()
            }

        binding.createTodoButton
            .setOnClickListener {

                requireActivity()
                    .findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                        R.id.bottomNavigation
                    )
                    .selectedItemId = R.id.menu_create
            }

        loadTodos()
    }

    private fun loadTodos() {

        val token =
            TokenManager(
                requireContext()
            ).getToken()

        lifecycleScope.launch {

            try {
                binding.toggleCompletedButton.isEnabled = false

                val todos =
                    ApiClient
                        .create()
                        .getTodos(
                            "Bearer $token"
                        )

                if (todos.isEmpty()) {

                    binding.toggleCompletedButton
                        .visibility = View.GONE

                } else {

                    binding.toggleCompletedButton
                        .visibility = View.VISIBLE
                }

                val sortedTodos =
                    todos.sortedWith(

                        compareBy<Todo> {

                            LocalDate.parse(
                                it.time.end.substring(0, 10)
                            )
                        }

                            // 종료일 같으면 시작일 빠른 순
                            .thenBy {

                                LocalDate.parse(
                                    it.time.start.substring(0, 10)
                                )
                            }

                            // 중요도 높은 순
                            .thenByDescending {

                                getImportanceScore(
                                    it.importance
                                )
                            }

                            // 이름 오름차순
                            .thenBy {
                                it.name
                            }
                    )

                val filteredTodos =

                    if (showCompleted) {

                        sortedTodos.filter {
                            it.is_completed
                        }

                    } else {

                        sortedTodos.filter {
                            !it.is_completed
                        }
                    }

                binding.toggleCompletedButton.text =

                    if (showCompleted) {

                        "완료되지 않은 TODO 보기"

                    } else {

                        "완료된 TODO 보기"
                    }

                if (filteredTodos.isEmpty()) {

                    binding.emptyLayout
                        .visibility = View.VISIBLE

                    binding.todoRecyclerView
                        .visibility = View.GONE

                    if (showCompleted) {

                        // 완료 TODO 화면
                        binding.emptyText.text =
                            "완료된 할 일이 없습니다"

                        binding.createTodoButton
                            .visibility = View.GONE

                    } else {

                        // 미완료 TODO 화면
                        binding.emptyText.text =
                            "할 일이 없습니다"

                        binding.createTodoButton
                            .visibility = View.VISIBLE
                    }

                } else {

                    binding.emptyLayout
                        .visibility = View.GONE

                    binding.todoRecyclerView
                        .visibility = View.VISIBLE

                    adapter.updateTodos(filteredTodos)
                }

                binding.toggleCompletedButton.isEnabled = true

            } catch (e: Exception) {
                binding.toggleCompletedButton.isEnabled = true

                e.printStackTrace()
            }
        }
    }

    private fun completeTodo(
        todo: Todo
    ) {

        val token =
            TokenManager(
                requireContext()
            ).getToken()

        lifecycleScope.launch {

            try {

                val response =
                    ApiClient
                        .create()
                        .updateTodo(

                            "Bearer $token",

                            todo.id,

                            UpdateTodoRequest(

                                name =
                                    todo.name,

                                description =
                                    todo.description,

                                base_value =
                                    todo.base_value,

                                importance =
                                    todo.importance,

                                is_completed =
                                    true
                            )
                        )

                Toast.makeText(

                    requireContext(),

                    "TODO 완료! +${response.reward_gold} Gold 획득",

                    Toast.LENGTH_LONG

                ).show()

                loadTodos()

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    private fun showDeleteDialog(
        todo: Todo
    ) {

        AlertDialog.Builder(
            requireContext()
        )
            .setTitle("TODO 삭제")
            .setMessage(
                "삭제할 경우 복구가 불가능합니다."
            )

            .setPositiveButton(
                "OK"
            ) { _, _ ->

                deleteTodo(todo)
            }

            .setNegativeButton(
                "CANCEL",
                null
            )

            .show()
    }

    private fun deleteTodo(
        todo: Todo
    ) {

        val token =
            TokenManager(
                requireContext()
            ).getToken()

        lifecycleScope.launch {

            try {

                ApiClient
                    .create()
                    .deleteTodo(

                        "Bearer $token",

                        todo.id
                    )

                loadTodos()

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    private fun getImportanceScore(
        importance: String
    ): Int {

        return when (importance) {

            "LOWEST" -> 1

            "LOW" -> 2

            "MID_LOW" -> 3

            "MID" -> 4

            "MID_HIGH" -> 5

            "HIGH" -> 6

            else -> 7
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }

    override fun onResume() {

        super.onResume()

        loadTodos()
    }
}