package edu.skku.cs.personalproject.Fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

import java.util.Calendar

import edu.skku.cs.personalproject.APIs.ApiClient
import edu.skku.cs.personalproject.DTOs.TokenManager
import edu.skku.cs.personalproject.DTOs.Todo.CreateTodoRequest
import edu.skku.cs.personalproject.DTOs.Todo.TodoTime
import edu.skku.cs.personalproject.R
import edu.skku.cs.personalproject.databinding.FragmentCreateTodoBinding

import kotlinx.coroutines.launch

class CreateTodoFragment : Fragment() {

    private var _binding:
            FragmentCreateTodoBinding? = null

    private val binding
        get() = _binding!!

    private var selectedStartDate: Calendar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            FragmentCreateTodoBinding.inflate(
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

        setupBaseValueFilter()

        setupDatePicker()

        binding.saveButton
            .setOnClickListener {

                createTodo()
            }

        binding.radioMid.isChecked = true
    }

    private fun setupBaseValueFilter() {

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
    }

    private fun setupDatePicker() {

        binding.startDateEditText
            .setOnClickListener {

                showStartDatePicker()
            }

        binding.endDateEditText
            .setOnClickListener {

                showEndDatePicker()
            }
    }

    private fun showStartDatePicker() {

        val calendar = Calendar.getInstance()

        DatePickerDialog(

            requireContext(),

            { _, year, month, day ->

                val selectedCalendar =
                    Calendar.getInstance()

                selectedCalendar.set(
                    year,
                    month,
                    day
                )

                selectedStartDate =
                    selectedCalendar

                val formattedDate = String.format(

                    "%04d-%02d-%02dT00:00:00",

                    year,
                    month + 1,
                    day
                )

                binding.startDateEditText
                    .setText(formattedDate)

                // 종료 날짜 초기화
                binding.endDateEditText
                    .setText("")
            },

            calendar.get(Calendar.YEAR),

            calendar.get(Calendar.MONTH),

            calendar.get(Calendar.DAY_OF_MONTH)

        ).apply {

            // 오늘 이전 날짜 선택 불가
            datePicker.minDate =
                System.currentTimeMillis()

        }.show()
    }

    private fun showEndDatePicker() {

        if (selectedStartDate == null) {

            Toast.makeText(

                requireContext(),

                "먼저 시작 날짜를 선택하세요",

                Toast.LENGTH_SHORT

            ).show()

            return
        }

        val startCalendar =
            selectedStartDate!!.clone() as Calendar

        // 시작일 + 1일
        startCalendar.add(
            Calendar.DAY_OF_MONTH,
            1
        )

        DatePickerDialog(

            requireContext(),

            { _, year, month, day ->

                val formattedDate = String.format(

                    "%04d-%02d-%02dT00:00:00",

                    year,
                    month + 1,
                    day
                )

                binding.endDateEditText
                    .setText(formattedDate)
            },

            startCalendar.get(Calendar.YEAR),

            startCalendar.get(Calendar.MONTH),

            startCalendar.get(Calendar.DAY_OF_MONTH)

        ).apply {

            // 시작 날짜 + 1일부터만 가능
            datePicker.minDate =
                startCalendar.timeInMillis

        }.show()
    }

    private fun getSelectedImportance():
            String {

        return when (
            binding
                .importanceRadioGroup
                .checkedRadioButtonId
        ) {

            R.id.radioLowest ->
                "LOWEST"

            R.id.radioLow ->
                "LOW"

            R.id.radioMidLow ->
                "MID_LOW"

            R.id.radioMid ->
                "MID"

            R.id.radioMidHigh ->
                "MID_HIGH"

            R.id.radioHigh ->
                "HIGH"

            else ->
                "HIGHEST"
        }
    }

    private fun createTodo() {

        val token =
            TokenManager(
                requireContext()
            ).getToken()

        val name =
            binding.nameEditText.text
                .toString()

        val description =
            binding.descriptionEditText.text
                .toString()

        val startDate =
            binding.startDateEditText.text
                .toString()

        val endDate =
            binding.endDateEditText.text
                .toString()

        val baseValueText =
            binding.baseValueEditText.text
                .toString()

        if (
            name.isBlank() ||
            description.isBlank() ||
            startDate.isBlank() ||
            endDate.isBlank() ||
            baseValueText.isBlank()
        ) {

            Toast.makeText(

                requireContext(),

                "모든 항목을 입력하세요",

                Toast.LENGTH_SHORT

            ).show()

            return
        }

        val request =
            CreateTodoRequest(

                name =
                    name,

                description =
                    description,

                time =
                    TodoTime(

                        startDate,

                        endDate
                    ),

                importance =
                    getSelectedImportance(),

                base_value =
                    baseValueText.toInt()
            )

        lifecycleScope.launch {

            try {

                ApiClient
                    .create()
                    .createTodo(

                        "Bearer $token",

                        request
                    )

                Toast.makeText(

                    requireContext(),

                    "TODO 생성 완료",

                    Toast.LENGTH_SHORT

                ).show()

                // 입력값 초기화
                clearInputs()

// TODOs 화면 이동
                requireActivity()
                    .findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                        R.id.bottomNavigation
                    )
                    .selectedItemId = R.id.menu_todos

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(

                    requireContext(),

                    "생성 실패",

                    Toast.LENGTH_SHORT

                ).show()
            }
        }
    }

    private fun clearInputs() {

        binding.nameEditText.text
            ?.clear()

        binding.descriptionEditText.text
            ?.clear()

        binding.baseValueEditText.text
            ?.clear()

        binding.startDateEditText.text
            ?.clear()

        binding.endDateEditText.text
            ?.clear()

        binding.importanceRadioGroup
            .clearCheck()

        binding.radioMid.isChecked = true
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }

    override fun onStop() {
        super.onStop()
        clearInputs()
        selectedStartDate = null
    }
}