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
import com.example.todolist.ui.database.instance.DatabaseInstance
import com.example.todolist.ui.database.model.Task
import com.example.todolist.ui.database.repository.TaskRepository
import com.google.android.material.textfield.TextInputLayout

class AddFragment : Fragment() {

    private lateinit var addViewModel: AddViewModel

    private lateinit var binding: FragmentAddBinding

    private val titleText: String
        get() = binding.textFieldTitleText.text?.toString() ?: ""

    private val annotationText: String
        get() = binding.textFieldAnotationText.text?.toString() ?: ""

    private val isTitleValid
        get() = titleText.length <= 50 && titleText.isNotBlank()

    private val isAnnotationValid
        get() = annotationText.length <= 300 && annotationText.isNotBlank()

    private val isTitleValidColorIcon
        get() = titleText.length <= 50

    private val isAnnotationValidColorIcon
        get() = annotationText.length <= 300

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        val root = binding.root

        setupAddViewModel()
        updateButton()
        verificationChar()
        clickSaveButton()
        openBottomSheet()

        return root
    }

    private fun setupAddViewModel() {
        val database = DatabaseInstance.getDatabase(requireContext())
        val repository = TaskRepository(database.taskDao())
        val factory = AddViewModelFactory(repository)
        addViewModel = ViewModelProvider(this, factory)[AddViewModel::class.java]
    }

    private fun updateButton() {
        with(binding) {
            val shouldEnableButton = isTitleValid && isAnnotationValid
            saveButton.isEnabled = shouldEnableButton
            saveButton.alpha = if (shouldEnableButton) 1f else 0.5f
        }
    }

    private fun verificationChar() {
        with(binding) {
            textFieldTitleText.doOnTextChanged { _, _, _, _ ->
                updateButton()
                updateTextInputColor(textFieldTitle, isTitleValidColorIcon)
            }

            textFieldAnotationText.doOnTextChanged { _, _, _, _ ->
                updateButton()
                updateTextInputColor(textFieldAnotation, isAnnotationValidColorIcon)
            }
        }
    }

    private fun updateTextInputColor(textInput: TextInputLayout, isValid: Boolean) {
        textInput.setEndIconTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    if (isValid) R.color.orange_01 else R.color.red_error
                )
            )
        )
    }

    private fun clickSaveButton() {
        with(binding) {
            saveButton.setOnClickListener {
                val title = textFieldTitleText.text.toString()
                val description = textFieldAnotationText.text.toString()
                val newTask = Task(title = title, description = description)
                addViewModel.insertTask(newTask)
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

                Toast.makeText(requireContext(), getText(R.string.save_success), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openBottomSheet() {
        binding.anexoButton.setOnClickListener {
            val bottomSheet = AddBottomSheet(object : AddBottomSheet.PhotoPickerListener {
                override fun onCameraSelected() {

                }

                override fun onGallerySelected() {

                }
            })
            bottomSheet.show(parentFragmentManager, "AddBottomSheet")
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentXmlAdd.setOnTouchListener { _, event ->
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
        binding
    }
}
