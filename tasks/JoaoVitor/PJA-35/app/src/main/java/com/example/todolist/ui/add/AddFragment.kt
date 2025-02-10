package com.example.todolist.ui.add

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.todolist.R
import com.example.todolist.databinding.FragmentAddBinding
import com.example.todolist.ui.database.db.AppDatabase
import com.example.todolist.ui.database.instance.DatabaseInstance
import com.example.todolist.ui.database.model.Task

class AddFragment : Fragment() {

    private lateinit var database: AppDatabase
    private lateinit var addViewModel: AddViewModel

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val titleText: String
        get() = binding.textFieldTitleText.text?.toString() ?: ""

    private val annotationText: String
        get() = binding.textFieldAnotationText.text?.toString() ?: ""

    private val isTitleValid
        get() = titleText.length <= 50 && titleText.isNotBlank()

    private val isAnnotationValid
        get() = annotationText.length <= 300 && annotationText.isNotBlank()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val root = binding.root

        // Inicializar o banco de dados
        database = DatabaseInstance.getDatabase(requireContext())

        // Inicializar o ViewModel
        val factory = AddViewModelFactory(database.taskDao())
        addViewModel = ViewModelProvider(this, factory)[AddViewModel::class.java]

        updateButton()
        verificationChar()
        saveTask()

        return root
    }

    private fun updateButton() {
        with(binding) {
            val shouldEnableButton = isTitleValid && isAnnotationValid
            outlinedButton.isEnabled = shouldEnableButton
            outlinedButton.alpha = if (shouldEnableButton) 1f else 0.5f
        }
    }

    private fun verificationChar() {
        with(binding) {
            textFieldTitleText.doOnTextChanged { _, _, _, _ ->
                updateButton()

                textFieldTitle.setEndIconTintList(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            if (!isTitleValid) R.color.red_error else R.color.orange_01
                        )
                    )
                )
            }

            textFieldAnotationText.doOnTextChanged { _, _, _, _ ->
                updateButton()

                textFieldAnotation.setEndIconTintList(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            if (!isAnnotationValid) R.color.red_error else R.color.orange_01
                        )
                    )
                )
            }
        }
    }

    private fun saveTask() {
        with(binding) {
            outlinedButton.setOnClickListener {
                val title = textFieldTitleText.text.toString()
                val description = textFieldAnotationText.text.toString()
                val newTask = Task(title = title, description = description)
                addViewModel.inserirTask(newTask)
                textFieldTitleText.text?.clear()
                textFieldAnotationText.text?.clear()

                // Esconder teclado
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                val view = requireActivity().currentFocus
                view?.let {
                    inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
                    it.clearFocus()
                }

                val viewPager = requireActivity().findViewById<ViewPager2>(R.id.vp)
                viewPager.currentItem = 0

                Toast.makeText(requireContext(), getText(R.string.save_success), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rootLayout: View = view.findViewById(R.id.fragmentXmlAdd)

        rootLayout.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val focusedView = requireActivity().currentFocus
                if (focusedView is EditText) {
                    focusedView.clearFocus()
                    hideKeyboard(focusedView)
                }
            }
            false
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
