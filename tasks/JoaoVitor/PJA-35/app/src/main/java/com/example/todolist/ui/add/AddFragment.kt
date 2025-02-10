package com.example.todolist.ui.add

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
                Toast.makeText(requireContext(), getText(R.string.save_success), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
