package com.example.todo.add

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.todo.R
import com.example.todo.databinding.FragmentAddBinding
import com.example.todo.room.DataBase
import com.example.todo.room.Task
import com.example.todo.room.TaskDao

class AddFragment : Fragment() {

    private var binding: FragmentAddBinding? = null
    private var addViewModel: AddViewModel? = null
    private var db: DataBase? = null
    private var taskDao: TaskDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        addViewModel = ViewModelProvider(this)[AddViewModel::class.java]
        db = DataBase.getInstance(context)
        taskDao = db?.taskDao()

        Log.e("teste", "passou por aqui muito antes")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddBinding.inflate(layoutInflater)
        setupListener()
        return binding?.root

    }

    private fun setupListener() {

        setupSaveButtonListener()

    }

    private fun setupSaveButtonListener() {

        val title = binding?.inputLayoutAddTitle?.editText?.text ?: getString(R.string.default_error_title)
        val content = binding?.inputLayoutAddContent?.editText?.text ?: getString(R.string.default_error_content)

        binding?.buttonSave?.setOnClickListener { addViewModel?.addTask(
            title = title.toString(),
            content = content.toString(),
            taskDao = taskDao ?: return@setOnClickListener
        )}

    }

}