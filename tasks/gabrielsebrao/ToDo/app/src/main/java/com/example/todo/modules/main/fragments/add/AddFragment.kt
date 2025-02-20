package com.example.todo.modules.main.fragments.add

import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todo.R
import com.example.todo.databinding.FragmentAddBinding
import com.example.todo.modules.main.MainViewModel

private const val EDIT_TEXT_TITLE = "edit_text_title"
private const val EDIT_TEXT_CONTENT = "edit_text_content"

class AddFragment : Fragment() {

    private var binding: FragmentAddBinding? = null
    private var mainViewModel: MainViewModel? = null

    companion object {
        const val TAG = "add_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddBinding.inflate(layoutInflater)

        if(savedInstanceState != null) {
            binding?.inputLayoutAddTitle?.editText?.setText(savedInstanceState.getString(
                EDIT_TEXT_TITLE
            ))
            binding?.inputLayoutAddContent?.editText?.setText(savedInstanceState.getString(
                EDIT_TEXT_CONTENT
            ))
        }

        setupListener()
        return binding?.root

    }

    override fun onSaveInstanceState(outState: Bundle) {

        super.onSaveInstanceState(outState)

        outState.putString(EDIT_TEXT_TITLE, binding?.inputLayoutAddTitle?.editText?.text.toString())
        outState.putString(EDIT_TEXT_CONTENT, binding?.inputLayoutAddContent?.editText?.text.toString())

    }

    private fun setupListener() {

        setupSaveButtonListener()
        setupInputLayoutsListener()
        setupRootListener()

    }

    private fun setupSaveButtonListener() {

        if(binding?.inputLayoutAddTitle?.editText?.text?.isNotBlank() == true && binding?.inputLayoutAddContent?.editText?.text?.isNotBlank() == true)
            binding?.buttonSave?.isActivated = true

        binding?.buttonSave?.setOnClickListener {

            if(!it.isActivated)
                return@setOnClickListener

            if(binding?.inputLayoutAddTitle?.editText?.text?.isBlank() == true) {
                binding?.inputLayoutAddTitle?.error = getString(R.string.error_add_title)
                it.isActivated = false
                return@setOnClickListener
            }

            mainViewModel?.addTask(
                binding?.inputLayoutAddTitle?.editText?.text.toString(),
                binding?.inputLayoutAddContent?.editText?.text.toString()
            )

            (activity?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(view?.windowToken, 0)

        }

    }

    private fun setupInputLayoutsListener() {

        filterMaxLength(binding?.inputLayoutAddTitle?.editText, 50)
        binding?.inputLayoutAddTitle?.editText?.addTextChangedListener(enableSaveButtonTaskTitle(binding))

        filterMaxLength(binding?.inputLayoutAddContent?.editText, 300)
        binding?.inputLayoutAddContent?.editText?.addTextChangedListener(enableSaveButtonTaskContent(binding))

    }

    private fun setupRootListener() {

        val rect = Rect()

        binding?.root?.viewTreeObserver?.addOnGlobalLayoutListener {
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

    private fun filterMaxLength(editText: EditText?, maxLength: Int) {

        if(editText == null)
            return

        editText.filters = arrayOf(InputFilter.LengthFilter(maxLength))

    }

    private fun enableSaveButtonTaskTitle(binding: FragmentAddBinding?): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(binding?.inputLayoutAddTitle?.editText?.text?.isBlank() == true ) {
                    binding.buttonSave.isActivated = false
                    return
                }

                if(binding?.inputLayoutAddContent?.editText?.text?.isBlank() == true ) {
                    binding.buttonSave.isActivated = false
                    return
                }

                binding?.buttonSave?.isActivated = true

            }
            override fun afterTextChanged(s: Editable?) {}
        }
    }

    private fun enableSaveButtonTaskContent(binding: FragmentAddBinding?): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(binding?.inputLayoutAddContent?.editText?.text?.isBlank() == true ) {
                    binding.buttonSave.isActivated = false
                    return
                }

                binding?.buttonSave?.isActivated = true

            }
            override fun afterTextChanged(s: Editable?) {}
        }
    }

    fun onAddTask() {

        binding?.inputLayoutAddTitle?.editText?.setText("")
        binding?.inputLayoutAddContent?.editText?.setText("")

    }

}