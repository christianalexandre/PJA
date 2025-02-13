package com.example.todo.archived

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.TaskSingleton
import com.example.todo.adapter.ArchivedTaskAdapter
import com.example.todo.databinding.FragmentArchivedBinding
import com.example.todo.main.MainViewModel
import com.example.todo.room.DataBase
import com.example.todo.room.Task
import com.example.todo.room.TaskDao

class ArchivedFragment : Fragment() {

    private var binding: FragmentArchivedBinding? = null
    private var mainViewModel: MainViewModel? = null
    private var db: DataBase? = null
    private var taskDao: TaskDao? = null
    private var archivedTaskAdapter: ArchivedTaskAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        db = DataBase.getInstance(context)
        taskDao = db?.taskDao()
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        setupObservers()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentArchivedBinding.inflate(layoutInflater)
        return binding?.root

    }

    private fun setupObservers() {

        mainViewModel?.isGetAllTasksSuccess?.observe(this) { isSuccess ->

            if(!isSuccess)
                return@observe

            if(archivedTaskAdapter == null) {
                archivedTaskAdapter = ArchivedTaskAdapter(TaskSingleton.archivedTaskList ?: emptyList<Task>().toMutableList())
                binding?.recyclerViewTasks?.adapter = archivedTaskAdapter
                binding?.recyclerViewTasks?.layoutManager = LinearLayoutManager(context)
            }

            if(archivedTaskAdapter?.taskList?.isEmpty() == true) {
                displayDefaultScreen()
                return@observe
            }

            displayRecyclerViewScreen()

        }

        mainViewModel?.isArchiveTaskSuccess?.observe(this) { isSuccess ->

            if(!isSuccess)
                return@observe

            archivedTaskAdapter?.addNewTask(TaskSingleton.archivedTask)
            binding?.recyclerViewTasks?.scrollToPosition(0)

            if(archivedTaskAdapter?.taskList?.isEmpty() == true) {
                displayDefaultScreen()
                return@observe
            }

            displayRecyclerViewScreen()

        }



    }

    private fun displayDefaultScreen() {

        binding?.defaultEmptyTaskList?.visibility = View.VISIBLE
        binding?.recyclerViewTasks?.visibility = View.GONE

    }

    private fun displayRecyclerViewScreen() {

        binding?.defaultEmptyTaskList?.visibility = View.GONE
        binding?.recyclerViewTasks?.visibility = View.VISIBLE

    }

}