package edu.skku.cs.personalproject

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import edu.skku.cs.personalproject.Fragments.CreateTodoFragment
import edu.skku.cs.personalproject.Fragments.MyInfoFragment
import edu.skku.cs.personalproject.Fragments.TodoListFragment

import edu.skku.cs.personalproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityMainBinding

    private val todoListFragment =
        TodoListFragment()

    private val createTodoFragment =
        CreateTodoFragment()

    private val myInfoFragment =
        MyInfoFragment()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityMainBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        // 기본 화면
        replaceFragment(
            todoListFragment
        )

        handleIntentFragment()

        binding.bottomNavigation
            .setOnItemSelectedListener {

                when (it.itemId) {

                    R.id.menu_todos -> {

                        replaceFragment(
                            todoListFragment
                        )

                        true
                    }

                    R.id.menu_create -> {

                        replaceFragment(
                            createTodoFragment
                        )

                        true
                    }

                    R.id.menu_myinfo -> {

                        replaceFragment(
                            myInfoFragment
                        )

                        true
                    }

                    else -> false
                }
            }
    }

    private fun replaceFragment(
        fragment: Fragment
    ) {

        supportFragmentManager
            .beginTransaction()

            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )

            .replace(
                R.id.fragmentContainer,
                fragment
            )

            .commit()
    }

    private fun handleIntentFragment() {

        val target =
            intent.getStringExtra(
                "open_fragment"
            )

        if (target == "todos") {

            binding.bottomNavigation
                .selectedItemId =
                R.id.menu_todos
        }
    }
}