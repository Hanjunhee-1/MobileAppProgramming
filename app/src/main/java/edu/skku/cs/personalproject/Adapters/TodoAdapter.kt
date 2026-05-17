package edu.skku.cs.personalproject.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView

import edu.skku.cs.personalproject.DTOs.Todo.Todo
import edu.skku.cs.personalproject.databinding.ItemTodoBinding

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class TodoAdapter(

    private val todos: MutableList<Todo>,

    private val onClick:
        (Todo) -> Unit,

    private val onComplete:
        (Todo) -> Unit,

    private val onDelete:
        (Todo) -> Unit

) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    inner class ViewHolder(

        val binding: ItemTodoBinding

    ) : RecyclerView.ViewHolder(
        binding.root
    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding =
            ItemTodoBinding.inflate(

                LayoutInflater.from(
                    parent.context
                ),

                parent,
                false
            )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val todo = todos[position]

        holder.binding.todoNameText.text =
            todo.name

        holder.binding.descriptionText.text =
            todo.description

        holder.binding.valueText.text =
            "Value: ${todo.current_value}"

        holder.binding.importanceText.text =
            todo.importance

        holder.binding.startDateText.text =
            "시작: ${
                todo.time.start.substring(0, 10)
            }"

        holder.binding.endDateText.text =
            "마감: ${
                todo.time.end.substring(0, 10)
            }"

        // 중요도 색상 처리
        val importanceColor =
            when (todo.importance) {

                "LOWEST" ->
                    "#9E9E9E"

                "LOW" ->
                    "#42A5F5"

                "MID_LOW" ->
                    "#26C6DA"

                "MID" ->
                    "#FFD700"

                "MID_HIGH" ->
                    "#FFB74D"

                "HIGH" ->
                    "#FF7043"

                else ->
                    "#F44336"
            }

        holder.binding.importanceText
            .setTextColor(

                Color.parseColor(
                    importanceColor
                )
            )

        val startDate =
            LocalDate.parse(
                todo.time.start.substring(0, 10)
            )

        val endDate =
            LocalDate.parse(
                todo.time.end.substring(0, 10)
            )

        val today =
            LocalDate.now()

        // D-day 처리
        // D-day 처리
        if (today.isBefore(startDate)) {

            holder.binding.ddayText.text =
                "시작 전"

        } else {

            val dday =
                ChronoUnit.DAYS.between(
                    today,
                    endDate
                )

            holder.binding.ddayText.text =
                "D-$dday"
        }

        // 완료 여부 스타일 처리
        if (todo.is_completed) {

            holder.binding.todoCardView
                .setCardBackgroundColor(
                    Color.parseColor("#B3E5FC")
                )

            holder.binding.todoNameText
                .setTextColor(
                    Color.parseColor("#0D47A1")
                )

            holder.binding.descriptionText
                .setTextColor(
                    Color.parseColor("#1565C0")
                )

            holder.binding.valueText
                .setTextColor(
                    Color.parseColor("#01579B")
                )

            holder.binding.completeButton
                .visibility = View.GONE

            holder.binding.deleteButton
                .visibility = View.GONE

        } else {

            holder.binding.todoCardView
                .setCardBackgroundColor(
                    Color.parseColor("#1E1E1E")
                )

            holder.binding.todoNameText
                .setTextColor(
                    Color.WHITE
                )

            holder.binding.descriptionText
                .setTextColor(
                    Color.parseColor("#BDBDBD")
                )

            holder.binding.valueText
                .setTextColor(
                    Color.parseColor("#FFD700")
                )

            holder.binding.completeButton
                .visibility = View.VISIBLE

            holder.binding.deleteButton
                .visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {

            onClick(todo)
        }

        holder.binding.completeButton
            .setOnClickListener {

                onComplete(todo)
            }

        holder.binding.deleteButton
            .setOnClickListener {

                onDelete(todo)
            }
    }

    override fun getItemCount(): Int {

        return todos.size
    }

    fun updateTodos(
        newTodos: List<Todo>
    ) {

        todos.clear()

        todos.addAll(newTodos)

        notifyDataSetChanged()
    }
}