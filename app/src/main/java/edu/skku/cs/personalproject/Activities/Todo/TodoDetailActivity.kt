package edu.skku.cs.personalproject.Activities.Todo

import android.content.Intent
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

import edu.skku.cs.personalproject.APIs.ApiClient
import edu.skku.cs.personalproject.Activities.Login.TokenManager
import edu.skku.cs.personalproject.databinding.ActivityTodoDetailBinding

import kotlinx.coroutines.launch

class TodoDetailActivity :
    AppCompatActivity() {

    private lateinit var binding:
            ActivityTodoDetailBinding

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityTodoDetailBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        val todoId =
            intent.getIntExtra(
                "todo_id",
                -1
            )

        val todoName =
            intent.getStringExtra(
                "todo_name"
            ) ?: "TODO DETAIL"

        val isCompleted =
            intent.getBooleanExtra(
                "is_completed",
                false
            )

        // 완료된 TODO면 수정 버튼 숨김
        if (isCompleted) {

            binding.editButton.visibility =
                View.GONE
        }

        binding.titleTextView.text = todoName

        binding.editButton
            .setOnClickListener {

                val intent =
                    Intent(
                        this,
                        TodoEditActivity::class.java
                    )

                intent.putExtra(
                    "todo_id",
                    todoId
                )

                intent.putExtra(
                    "todo_name",
                    intent.getStringExtra(
                        "todo_name"
                    )
                )

                intent.putExtra(
                    "is_completed",
                    isCompleted
                )

                startActivity(intent)

                overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
            }

        loadValueHistory(todoId, isCompleted)
    }

    private fun loadValueHistory(
        todoId: Int,
        isCompleted: Boolean
    ) {

        val token =
            TokenManager(this)
                .getToken()

        lifecycleScope.launch {

            try {

                val histories =
                    ApiClient
                        .create()
                        .getHistories(

                            "Bearer $token",

                            todoId
                        )

                if (histories.isEmpty()) {

                    return@launch
                }

                if (isCompleted) {

                    val rewardGold =
                        intent.getIntExtra(
                            "reward_gold",
                            0
                        )

                    binding.currentValueText.text =
                        "Reward : ${
                            rewardGold
                        }"
                } else {
                    binding.currentValueText.text =
                        "Current Value : ${
                            histories.last().value
                        }"
                }

                setupStatistics(histories)

                setupChart(histories)

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    private fun setupChart(
        histories: List<
                edu.skku.cs.personalproject.DTOs.History.ValueHistory
                >
    ) {

        val entries =
            histories.mapIndexed {

                    index,
                    history ->

                Entry(
                    index.toFloat(),
                    history.value.toFloat()
                )
            }

        val dataSet =
            LineDataSet(
                entries,
                "Value History"
            )

        dataSet.color =
            android.graphics.Color
                .parseColor("#FFD700")

        dataSet.valueTextColor =
            android.graphics.Color.WHITE

        dataSet.lineWidth = 3f

        dataSet.circleRadius = 5f

        dataSet.setCircleColor(
            android.graphics.Color
                .parseColor("#FFD700")
        )

        dataSet.mode =
            LineDataSet.Mode
                .CUBIC_BEZIER

        val lineData =
            LineData(dataSet)

        binding.lineChart.data =
            lineData

        binding.lineChart.description
            .isEnabled = false

        binding.lineChart.legend
            .isEnabled = false

        binding.lineChart.setTouchEnabled(true)

        binding.lineChart.animateX(1200)

        binding.lineChart.invalidate()

        binding.lineChart.setOnChartValueSelectedListener(

            object :
                com.github.mikephil.charting.listener
                .OnChartValueSelectedListener {

                override fun onValueSelected(

                    e: Entry?,

                    h: com.github.mikephil.charting.highlight.Highlight?
                ) {

                    if (e == null) {
                        return
                    }

                    val index =
                        e.x.toInt()

                    val selected =
                        histories[index]

                    binding.selectedPointText.text =

                        "DATE : ${
                            selected.created_at.substring(0, 10)
                        }\nVALUE : ${
                            selected.value
                        }"
                }

                override fun onNothingSelected() {

                }
            }
        )
    }

    private fun setupStatistics(

        histories: List<
                edu.skku.cs.personalproject.DTOs.History.ValueHistory
                >
    ) {

        val values =
            histories.map { it.value }

        val min =
            values.min()

        val max =
            values.max()

        val avg =
            values.average()

        val sorted =
            values.sorted()

        val median =
            if (sorted.size % 2 == 0) {

                (
                        sorted[
                            sorted.size / 2 - 1
                        ] +
                                sorted[
                                    sorted.size / 2
                                ]
                        ) / 2.0

            } else {

                sorted[
                    sorted.size / 2
                ].toDouble()
            }

        binding.minText.text =
            "MIN : $min"

        binding.maxText.text =
            "MAX : $max"

        binding.avgText.text =
            "AVG : %.1f".format(avg)

        binding.medianText.text =
            "MEDIAN : %.1f".format(median)
    }
}