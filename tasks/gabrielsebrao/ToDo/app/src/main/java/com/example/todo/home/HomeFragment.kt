package com.example.todo.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.TaskSingleton
import com.example.todo.adapter.TaskAdapter
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.room.DataBase
import com.example.todo.room.TaskDao

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private var homeViewModel: HomeViewModel? = null
    private var db: DataBase? = null
    private var taskDao: TaskDao? = null
    private var taskAdapter: TaskAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        db = DataBase.getInstance(context)
        taskDao = db?.taskDao()
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
            .taskDao(taskDao)

        setupObservers()

        homeViewModel?.getAllTasks()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding?.root

    }

    fun onShown() {

        if(TaskSingleton.newTask == null)
            return

        if(TaskSingleton.taskStack == null)
            return

        taskAdapter?.addNewTask(TaskSingleton.newTask)

    }

    private fun setupObservers() {

        homeViewModel?.isSuccess?.observe(this) { isSuccess ->

            if(!isSuccess)
                return@observe

            Log.e("ROOM_DEBUG", "${TaskSingleton.taskStack}")

            if(TaskSingleton.taskStack?.isEmpty() == true) {
                binding?.defaultHomeEmptyTaskList?.visibility = View.VISIBLE
                binding?.recyclerViewTasks?.visibility = View.GONE
                binding?.errorNullTaskList?.visibility = View.GONE
                return@observe
            }

            binding?.defaultHomeEmptyTaskList?.visibility = View.GONE
            binding?.errorNullTaskList?.visibility = View.GONE
            binding?.recyclerViewTasks?.visibility = View.VISIBLE

            taskAdapter = TaskAdapter(TaskSingleton.taskStack ?: return@observe treatNullableTaskList())

            binding?.recyclerViewTasks?.adapter = taskAdapter
            binding?.recyclerViewTasks?.layoutManager = LinearLayoutManager(context)

            homeViewModel?.isSuccess?.value = false

        }

    }

    private fun treatNullableTaskList() {

        binding?.errorNullTaskList?.visibility = View.VISIBLE
        binding?.defaultHomeEmptyTaskList?.visibility = View.GONE
        binding?.recyclerViewTasks?.visibility = View.GONE

    }

    fun getAllTasks() = homeViewModel?.getAllTasks()

}