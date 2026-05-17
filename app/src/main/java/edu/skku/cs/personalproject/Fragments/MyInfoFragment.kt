package edu.skku.cs.personalproject.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

import edu.skku.cs.personalproject.APIs.ApiClient
import edu.skku.cs.personalproject.DTOs.TokenManager
import edu.skku.cs.personalproject.databinding.FragmentMyInfoBinding

import kotlinx.coroutines.launch

class MyInfoFragment : Fragment() {

    private var _binding:
            FragmentMyInfoBinding? = null

    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            FragmentMyInfoBinding.inflate(
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

        loadMyInfo()
    }

    private fun loadMyInfo() {

        val token =
            TokenManager(
                requireContext()
            ).getToken()

        lifecycleScope.launch {

            try {

                val response =
                    ApiClient
                        .create()
                        .getMyInfo(
                            "Bearer $token"
                        )

                binding.nameText.text =
                    response.name

                binding.goldText.text =
                    "${response.total_gold} Gold"

                binding.todoCountText.text =
                    response.todo_count
                        .toString()

                binding.completedTodoText.text =
                    response.completed_todo_count
                        .toString()

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(

                    requireContext(),

                    "내 정보 조회 실패",

                    Toast.LENGTH_SHORT

                ).show()
            }
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}