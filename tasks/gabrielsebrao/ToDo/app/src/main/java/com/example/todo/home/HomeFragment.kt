package com.example.todo.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todo.TaskSingleton
import com.example.todo.adapter.TaskAdapter
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.room.DataBase
import com.example.todo.room.Task
import com.example.todo.room.TaskDao
import com.example.todo.sharedpref.TaskListOrderSharedPref
import java.util.Collections

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private var homeViewModel: HomeViewModel? = null
    private var db: DataBase? = null
    private var taskDao: TaskDao? = null
    private var taskAdapter: TaskAdapter? = null
    private var taskListOrderSharedPref: TaskListOrderSharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        db = DataBase.getInstance(context)
        taskDao = db?.taskDao()
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
            .taskDao(taskDao)
        taskListOrderSharedPref = TaskListOrderSharedPref(requireContext())

        setupObservers()

        homeViewModel?.getAllTasks()

    }

    inner class ItemTouchHelper(dragDirs: Int, swipeDirs: Int) : androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean {
            val from = viewHolder.adapterPosition
            val to = target.adapterPosition

            taskAdapter?.taskList?.let { Collections.swap(it, from, to) }
            taskAdapter?.notifyItemMoved(from, to)

            val list: MutableList<Int> = emptyList<Int>().toMutableList()
            TaskSingleton.taskList?.map { list.add(it.id ?: 0) }
            taskListOrderSharedPref?.saveList(list)

            Log.d("SHARED_PREF", "TASK ID LIST: ${taskListOrderSharedPref?.list}")

            return true
        }

        override fun onSwiped(viewHolder: ViewHolder, direction: Int) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)

        val helper = androidx.recyclerview.widget.ItemTouchHelper(ItemTouchHelper(
            androidx.recyclerview.widget.ItemTouchHelper.UP or
                    androidx.recyclerview.widget.ItemTouchHelper.DOWN,
            androidx.recyclerview.widget.ItemTouchHelper.LEFT
        ))

        helper.attachToRecyclerView(binding?.recyclerViewTasks)

        return binding?.root

    }

    fun onShown() {

        if((TaskSingleton.taskList?.size ?: return) > 0)
            displayRecyclerViewScreen()

        taskAdapter?.addNewTask(TaskSingleton.newTask)
        TaskSingleton.newTask = null

        binding?.recyclerViewTasks?.scrollToPosition(0)

    }

    private fun setupObservers() {

        homeViewModel?.isSuccess?.observe(this) { isSuccess ->

            if(!isSuccess)
                return@observe

            TaskSingleton.taskList = TaskSingleton.taskList?.sortedBy { task ->
                taskListOrderSharedPref?.list?.indexOf(task.id)
            }?.toMutableList()

            if(taskAdapter == null) {
                taskAdapter = TaskAdapter(TaskSingleton.taskList ?: emptyList<Task>().toMutableList())
                binding?.recyclerViewTasks?.adapter = taskAdapter
                binding?.recyclerViewTasks?.layoutManager = LinearLayoutManager(context)
            }

            Log.e("ROOM_DEBUG", "${TaskSingleton.taskList}")

            homeViewModel?.isSuccess?.value = false

            if(TaskSingleton.taskList?.isEmpty() == true) {
                displayDefaultScreen()
                return@observe
            }

            displayRecyclerViewScreen()

        }

    }

    private fun displayRecyclerViewScreen() {

        binding?.defaultHomeEmptyTaskList?.visibility = View.GONE
        binding?.errorNullTaskList?.visibility = View.GONE
        binding?.recyclerViewTasks?.visibility = View.VISIBLE

    }

    private fun displayDefaultScreen() {

        binding?.defaultHomeEmptyTaskList?.visibility = View.VISIBLE
        binding?.recyclerViewTasks?.visibility = View.GONE
        binding?.errorNullTaskList?.visibility = View.GONE

    }

    private fun treatNullableTaskList() {

        binding?.errorNullTaskList?.visibility = View.VISIBLE
        binding?.defaultHomeEmptyTaskList?.visibility = View.GONE
        binding?.recyclerViewTasks?.visibility = View.GONE

    }

    fun getAllTasks() = homeViewModel?.getAllTasks()

}