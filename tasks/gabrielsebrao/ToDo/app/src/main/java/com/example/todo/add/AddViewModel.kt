package com.example.todo.add

import android.content.Context
import android.content.res.ColorStateList
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.todo.R
import com.example.todo.room.Task
import com.example.todo.room.TaskDao
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

private const val EMPTY_TEXT = ""

class AddViewModel: ViewModel() {

    private var taskDao: TaskDao? = null

    fun taskDao(taskDao: TaskDao?) = apply {
        this.taskDao = taskDao
    }

    fun addTask(title: String, content: String): Disposable? {

        val task: Task?

        try {
            task = Task(null, title, content)
        } catch(error: Exception) {
            Log.e("ROOM_DEBUG", "ADD TASK: ${error.message}")
            return null
        }

        return taskDao?.insertAll(task)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                Log.d("RX_DEBUG", "ADD TASK: OK")
            }, { error ->
                Log.e("RX_DEBUG", "ADD TASK: ${error.message}")
            })

    }

    fun maskTaskTitle(editText: EditText): TextWatcher {

        return object : TextWatcher {

            private var taskTitleMaxCharsPattern = "^(.{51,})$".toRegex()

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                editText.filters = arrayOf(InputFilter { source, begin, end, dest, dstart, dend ->

                    val result = dest.subSequence(0, dstart).toString() + source.subSequence(begin, end) + dest.subSequence(dend, dest.length).toString()

                    if(taskTitleMaxCharsPattern.matches(result))
                        return@InputFilter ""

                    null

                })

                editText.setSelection(editText.text.toString().length)

            }

            override fun afterTextChanged(s: Editable?) {}

        }

    }

    fun disableErrorTaskTitle(inputLayout: TextInputLayout?, context: Context): TextWatcher {

        return object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                inputLayout?.error = EMPTY_TEXT

            }

            override fun afterTextChanged(s: Editable?) {}

        }

    }

    fun maskTaskContent(editText: EditText): TextWatcher {

        return object : TextWatcher {

            private var taskContentMaxCharsPattern = "^(.{301,})$".toRegex()

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                editText.filters = arrayOf(InputFilter { source, begin, end, dest, dstart, dend ->

                    val result = dest.subSequence(0, dstart).toString() + source.subSequence(begin, end) + dest.subSequence(dend, dest.length).toString()

                    if(taskContentMaxCharsPattern.matches(result))
                        return@InputFilter ""

                    null

                })

                editText.setSelection(editText.text.toString().length)

            }

            override fun afterTextChanged(s: Editable?) {}

        }

    }

}