package com.example.todo.modules.main.fragments.add

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todo.R
import com.example.todo.databinding.FragmentAddBinding
import com.example.todo.modules.main.MainViewModel
import com.example.todo.utils.bottomsheet.BaseBottomSheetFragment
import com.example.todo.utils.bottomsheet.PhotoAccessBottomSheetFragment
import com.example.todo.utils.converter.Converters
import com.example.todo.utils.listener.PhotoAccessListener

private const val EDIT_TEXT_TITLE = "edit_text_title"
private const val EDIT_TEXT_CONTENT = "edit_text_content"

class AddFragment : Fragment(), PhotoAccessListener {

    private var binding: FragmentAddBinding? = null
    private var mainViewModel: MainViewModel? = null
    private var pickImageLauncher: ActivityResultLauncher<String>? = null
    private val bottomSheetFragment: PhotoAccessBottomSheetFragment = PhotoAccessBottomSheetFragment(this)
    private var cameraLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        setupPickImageLauncher()
        setupCameraLauncher()

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

    private fun setupPickImageLauncher() {

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                binding?.imageTaskText?.visibility = View.GONE
                binding?.taskImage?.setImageBitmap(resizeBitmap(turnUriIntoBitmap(it)))
            }
        }

    }

    private fun setupCameraLauncher() {

        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            if (result.resultCode != Activity.RESULT_OK)
                return@registerForActivityResult

            binding?.taskImage?.setImageBitmap(resizeBitmap(result.data?.extras?.get("data") as Bitmap))
            binding?.imageTaskText?.visibility = View.GONE

        }

    }

    private fun turnUriIntoBitmap(uri: Uri): Bitmap =
        BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(uri))

    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int = 500, maxHeight: Int = 700): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val scaleFactor = minOf(maxWidth.toFloat() / width, maxHeight.toFloat() / height)

        return Bitmap.createScaledBitmap(
            bitmap,
            (width * scaleFactor).toInt(),
            (height * scaleFactor).toInt(),
            true
        )
    }

    private fun setupListener() {

        setupSaveButtonListener()
        setupInputLayoutsListener()
        setupRootListener()
        setupSetImageButtonListener()

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
                binding?.inputLayoutAddContent?.editText?.text.toString(),
                Converters.bitmapToByteArray(binding?.taskImage?.drawable?.toBitmap(1000, 1000))
            )

            (activity?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(view?.windowToken, 0)

        }

    }

    private fun setupInputLayoutsListener() {

        filterMaxLength(binding?.inputLayoutAddTitle?.editText, 50)
        binding?.inputLayoutAddTitle?.editText?.addTextChangedListener(enableSaveButton())

        filterMaxLength(binding?.inputLayoutAddContent?.editText, 300)
        binding?.inputLayoutAddContent?.editText?.addTextChangedListener(enableSaveButton())

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

    private fun setupSetImageButtonListener() {

        binding?.buttonSetImage?.setOnClickListener {
            if(parentFragmentManager.fragments.any { it.tag == BaseBottomSheetFragment.TAG })
                return@setOnClickListener

            bottomSheetFragment.show(parentFragmentManager, BaseBottomSheetFragment.TAG)
        }

    }

    private fun filterMaxLength(editText: EditText?, maxLength: Int) {

        if(editText == null)
            return

        editText.filters = arrayOf(InputFilter.LengthFilter(maxLength))

    }

    private fun enableSaveButton(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(binding?.inputLayoutAddTitle?.editText?.text?.isBlank() == true ) {
                    binding?.buttonSave?.isActivated = false
                    return
                }

                if(binding?.inputLayoutAddContent?.editText?.text?.isBlank() == true ) {
                    binding?.buttonSave?.isActivated = false
                    return
                }

                binding?.buttonSave?.isActivated = true

            }
            override fun afterTextChanged(s: Editable?) {}
        }
    }

    override fun onAccessCamera() {
        cameraLauncher?.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
    }

    override fun onAccessGallery() {
        pickImageLauncher?.launch("image/*")
    }

    fun onAddTask() {

        binding?.inputLayoutAddTitle?.editText?.setText("")
        binding?.inputLayoutAddContent?.editText?.setText("")
        deleteImage()

    }

    private fun deleteImage() {

        binding?.imageTaskText?.visibility = View.VISIBLE
        binding?.taskImage?.setImageBitmap(null)

    }

}