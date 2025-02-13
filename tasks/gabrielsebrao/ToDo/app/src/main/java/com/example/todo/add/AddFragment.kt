package com.example.todo.add

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.todo.main.MainActivity
import com.example.todo.R
import com.example.todo.databinding.FragmentAddBinding
import com.example.todo.room.DataBase
import com.example.todo.task.TaskDao

private const val EDIT_TEXT_TITLE = "edit_text_title"
private const val EDIT_TEXT_CONTENT = "edit_text_content"

class AddFragment : Fragment() {

    private var binding: FragmentAddBinding? = null
    private var addViewModel: AddViewModel? = null
    private var db: DataBase? = null
    private var taskDao: TaskDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        db = DataBase.getInstance(context)
        taskDao = db?.taskDao()
        addViewModel = ViewModelProvider(this)[AddViewModel::class.java]
            .taskDao(taskDao)

        setupObserver()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddBinding.inflate(layoutInflater)

        if(savedInstanceState != null) {
            binding?.inputLayoutAddTitle?.editText?.setText(savedInstanceState.getString(EDIT_TEXT_TITLE))
            binding?.inputLayoutAddContent?.editText?.setText(savedInstanceState.getString(EDIT_TEXT_CONTENT))
        }

        setupListener()
        return binding?.root

    }

    override fun onSaveInstanceState(outState: Bundle) {

        super.onSaveInstanceState(outState)

        outState.putString(EDIT_TEXT_TITLE, binding?.inputLayoutAddTitle?.editText?.text.toString())
        outState.putString(EDIT_TEXT_CONTENT, binding?.inputLayoutAddContent?.editText?.text.toString())

    }

    private fun setupObserver() {

        addViewModel?.isSuccess?.observe(this) { isSuccess ->

            if(isSuccess) {
                (activity as MainActivity).switchFromAddFragmentToHomeFragment()
                addViewModel?.isSuccess?.value = false
            }

        }

    }

    private fun setupListener() {

        setupSaveButtonListener()
        setupInputLayoutsListener()
        setupRootListener()

    }

    private fun setupSaveButtonListener() {

        val title = binding?.inputLayoutAddTitle?.editText?.text ?: getString(R.string.default_error_title)
        val content = binding?.inputLayoutAddContent?.editText?.text ?: getString(R.string.default_error_content)

        if(title.isNotBlank() && content.isNotBlank())
            binding?.buttonSave?.isActivated = true

        binding?.buttonSave?.setOnClickListener {

            if(!it.isActivated)
                return@setOnClickListener

            if(title.isBlank()) {
                binding?.inputLayoutAddTitle?.error = getString(R.string.error_add_title)
                it.isActivated = false
                return@setOnClickListener
            }

            addViewModel?.addTask(
                title.toString(),
                content.toString(),
                requireContext()
            )

            Toast.makeText(context, getString(R.string.task_created), Toast.LENGTH_SHORT).show()

        }

    }

    private fun setupInputLayoutsListener() {

        addViewModel?.filterMaxLength(binding?.inputLayoutAddTitle?.editText, 50)
        binding?.inputLayoutAddTitle?.editText?.addTextChangedListener(addViewModel?.disableErrorTaskTitle(binding?.inputLayoutAddTitle))
        binding?.inputLayoutAddTitle?.editText?.addTextChangedListener(addViewModel?.enableSaveButtonTaskTitle(binding))

        addViewModel?.filterMaxLength(binding?.inputLayoutAddContent?.editText, 300)
        binding?.inputLayoutAddContent?.editText?.addTextChangedListener(addViewModel?.enableSaveButtonTaskContent(binding))

    }

    private fun setupRootListener() {

        binding?.root?.viewTreeObserver?.addOnGlobalLayoutListener {

            val rect = Rect()
            binding?.root?.getWindowVisibleDisplayFrame(rect)

            val screenHeight = binding?.root?.rootView?.height
            val keypadHeight = (screenHeight ?: 0) - rect.bottom
            val isKeyboardVisible = keypadHeight > ((screenHeight ?: 0) * 0.15)

            if(!isKeyboardVisible) {
                binding?.inputLayoutAddTitle?.clearFocus()
                binding?.inputLayoutAddContent?.clearFocus()
                return@addOnGlobalLayoutListener
            }

        }

    }

}