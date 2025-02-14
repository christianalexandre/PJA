package com.example.todo.add

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todo.task.TaskSingleton
import com.example.todo.databinding.FragmentAddBinding
import com.example.todo.task.Task
import com.example.todo.task.TaskDao
import com.example.todo.sharedpref.ToDoSharedPref
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

private const val EMPTY_TEXT = ""

class AddViewModel: ViewModel() {

    private var taskDao: TaskDao? = null
    val isSuccess: MutableLiveData<Boolean> = MutableLiveData(false)

    fun taskDao(taskDao: TaskDao?) = apply {
        this.taskDao = taskDao
    }

    fun addTask(title: String, content: String, context: Context): Disposable? {

        val toDoSharedPref = ToDoSharedPref.getInstance(context) ?: return null
        val task: Task?

        try {
            task = Task(toDoSharedPref.nextTaskId, title, content, false)
        } catch(error: Exception) {
            Log.e("ROOM_DEBUG", "ADD TASK: ${error.message}")
            return null
        }

        if(TaskSingleton.openTaskIdList == null)
            toDoSharedPref.saveOpenTaskIdList(mutableListOf(toDoSharedPref.nextTaskId))
        else
            toDoSharedPref.addOpenTaskIdToList(toDoSharedPref.nextTaskId)

        return taskDao?.insertAll(task)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({

                Log.d("RX_DEBUG", "ADD TASK: OK")
                Log.d("RX_DEBUG", "LIST FROM SHARED PREF: ${TaskSingleton.openTaskIdList}")
                Log.d("RX_DEBUG", "CURRENT TASK ID: ${toDoSharedPref.nextTaskId}")

                toDoSharedPref.incrementCurrentTaskId()
                TaskSingleton.newTask = task
                TaskSingleton.openTaskList?.add(0, task)

                Log.d("RX_DEBUG", "${TaskSingleton.openTaskList}")

                isSuccess.postValue(true)

            }, { error ->
                Log.e("RX_DEBUG", "ADD TASK: ${error.message}")
            })

    }

    fun filterMaxLength(editText: EditText?, maxLength: Int) {

        if(editText == null)
            return

        editText.filters = arrayOf(InputFilter.LengthFilter(maxLength))

    }

    fun enableSaveButtonTaskTitle(binding: FragmentAddBinding?): TextWatcher {

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

    fun enableSaveButtonTaskContent(binding: FragmentAddBinding?): TextWatcher {

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

    fun disableErrorTaskTitle(inputLayout: TextInputLayout?): TextWatcher {

        return object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                inputLayout?.error = EMPTY_TEXT

            }

            override fun afterTextChanged(s: Editable?) {}

        }

    }

}