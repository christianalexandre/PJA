package com.example.todolist.ui.add

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import android.Manifest
import com.example.todolist.R
import com.example.todolist.databinding.FragmentAddBinding
import com.example.todolist.ui.database.instance.DatabaseInstance
import com.example.todolist.ui.database.model.Task
import com.example.todolist.ui.database.repository.TaskRepository
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.io.FileOutputStream

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

    // Gerenciamento de permissões
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
            val storageGranted = permissions[getStoragePermission()] ?: false

            if (cameraGranted && storageGranted) {
                setOnclickAddFragmentPicture()
            } else {
                Toast.makeText(requireContext(), "Permissões negadas!", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)

        setupAddViewModel()
        updateButton()
        verificationChar()
        clickSaveButton()
        openBottomSheet()

        return binding.root
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
            textFieldTitleText.doOnTextChanged { text, _, _, _ ->
                if (text?.startsWith(" ") == true) {
                    textFieldTitleText.setText(text.toString().trimStart())
                    textFieldTitleText.setSelection(textFieldTitleText.text?.length ?: 0)
                }
                updateButton()
                updateTextInputColor(textFieldTitle, isTitleValidColorIcon)
            }

            textFieldAnotationText.doOnTextChanged { text, _, _, _ ->
                if (text?.startsWith(" ") == true) {
                    textFieldAnotationText.setText(text.toString().trimStart())
                    textFieldAnotationText.setSelection(textFieldAnotationText.text?.length ?: 0)
                }
                updateButton()
                updateTextInputColor(textFieldAnotation, isAnnotationValidColorIcon)
            }
        }
    }

    private fun updateTextInputColor(textInput: TextInputLayout, isValid: Boolean) {
        textInput.setEndIconTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), if (isValid) R.color.orange_01 else R.color.red_error)))
    }

    private fun clickSaveButton() {
        binding.saveButton.setOnClickListener {
            val title = titleText
            val description = annotationText
            val imageUri = (binding.picture.tag as? String)?.takeIf { it.isNotEmpty() }

            val newTask = Task(title = title, description = description, image = imageUri)

            addViewModel.insertTask(newTask)
            binding.textFieldTitleText.text?.clear()
            binding.textFieldAnotationText.text?.clear()
            removeAttachedImage()
            // Reset
            binding.picture.tag = null

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

    private fun removeAttachedImage() {
        with(binding) {
            picture.setImageDrawable(null)
            cardImageView.visibility = View.VISIBLE
            cardImageViewPicture.visibility = View.GONE
            deleteAttachButton.visibility = View.GONE
        }
    }

    private fun setOnclickAddFragmentPicture() {
        val bottomSheet = AddBottomSheet.newInstance()
        bottomSheet.setPhotoPickerListener(object : AddBottomSheet.PhotoPickerListener {
            override fun onPhotoSelected(uri: Uri?) {
                uri?.let {
                    binding.picture.setImageURI(it)
                    binding.picture.tag = it.toString()
                    binding.cardImageView.visibility = View.GONE
                    binding.cardImageViewPicture.visibility = View.VISIBLE
                    binding.deleteAttachButton.visibility = View.VISIBLE
                }
            }

            override fun onPhotoCaptured(bitmap: Bitmap?) {
                bitmap?.let {
                    val imageUri = saveBitmapToFile(it)
                    binding.picture.setImageBitmap(it)
                    binding.picture.tag = imageUri.toString()
                    binding.cardImageView.visibility = View.GONE
                    binding.cardImageViewPicture.visibility = View.VISIBLE
                    binding.deleteAttachButton.visibility = View.VISIBLE
                }
            }
        })

        bottomSheet.show(parentFragmentManager, "AddBottomSheet")
    }


    private fun saveBitmapToFile(bitmap: Bitmap): Uri {
        val filesDir = requireContext().filesDir
        val imageFile = File(filesDir, "task_image_${System.currentTimeMillis()}.jpg")

        FileOutputStream(imageFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }

        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            imageFile
        )
    }

    private fun openBottomSheet() {
        with(binding) {
            imageIcon.setOnClickListener {
                checkPermissionsAndOpenBottomSheet()
            }
            deleteAttachButton.setOnClickListener { removeAttachedImage() }
        }
    }

    private fun checkPermissionsAndOpenBottomSheet() {
        val cameraPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        )

        val storagePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            getStoragePermission()
        )

        if (cameraPermission == PackageManager.PERMISSION_GRANTED &&
            storagePermission == PackageManager.PERMISSION_GRANTED
        ) {
            setOnclickAddFragmentPicture()
        } else {
            requestPermissionsLauncher.launch(
                arrayOf(Manifest.permission.CAMERA, getStoragePermission())
            )
        }
    }

    private fun getStoragePermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }
}
