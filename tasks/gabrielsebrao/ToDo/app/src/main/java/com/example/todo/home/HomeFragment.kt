package com.example.todo.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.TaskSingleton
import com.example.todo.adapter.OpenTaskAdapter
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.room.DataBase
import com.example.todo.room.Task
import com.example.todo.room.TaskDao
import com.example.todo.sharedpref.ToDoSharedPref
import java.util.Collections

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private var homeViewModel: HomeViewModel? = null
    private var db: DataBase? = null
    private var taskDao: TaskDao? = null
    private var openTaskAdapter: OpenTaskAdapter? = null
    private var toDoSharedPref: ToDoSharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        db = DataBase.getInstance(context)
        taskDao = db?.taskDao()
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
            .taskDao(taskDao)
        toDoSharedPref = ToDoSharedPref.getInstance(context)

        setupObservers()

        homeViewModel?.getAllTasks()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)

        val helper = ItemTouchHelper(HomeItemTouchHelper(
            ItemTouchHelper.UP or
                    ItemTouchHelper.DOWN,
            0
        ))

        helper.attachToRecyclerView(binding?.recyclerViewTasks)

        return binding?.root

    }

    inner class HomeItemTouchHelper(
        dragDirs: Int,
        swipeDirs: Int
    ) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            val from = viewHolder.adapterPosition
            val to = target.adapterPosition

            openTaskAdapter?.taskList?.let { Collections.swap(it, from, to) }
            openTaskAdapter?.notifyItemMoved(from, to)

            val list: MutableList<Int> = emptyList<Int>().toMutableList()
            TaskSingleton.openTaskList?.map { list.add(it.id) }
            toDoSharedPref?.saveOpenTaskIdList(list)

            Log.d("SHARED_PREF", "TASK ID LIST: ${TaskSingleton.openTaskIdList}")

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    }

    fun onShown() {

        if((TaskSingleton.openTaskList?.size ?: return) > 0)
            displayRecyclerViewScreen()

        openTaskAdapter?.addNewTask(TaskSingleton.newTask)
        TaskSingleton.newTask = null

        binding?.recyclerViewTasks?.scrollToPosition(0)

    }

    private fun setupObservers() {

        homeViewModel?.isGetAllTasksSuccess?.observe(this) { isSuccess ->

            if(!isSuccess)
                return@observe

            if(openTaskAdapter == null) {
                openTaskAdapter = OpenTaskAdapter(TaskSingleton.openTaskList ?: emptyList<Task>().toMutableList(), homeViewModel)
                binding?.recyclerViewTasks?.adapter = openTaskAdapter
                binding?.recyclerViewTasks?.layoutManager = LinearLayoutManager(context)
            }

            homeViewModel?.isGetAllTasksSuccess?.value = false

            if(openTaskAdapter?.taskList?.isEmpty() == true) {
                displayDefaultScreen()
                return@observe
            }

            displayRecyclerViewScreen()

        }

        homeViewModel?.isDeleteTaskSuccess?.observe(this) { isSuccess ->

            if(!isSuccess)
                return@observe

            homeViewModel?.isDeleteTaskSuccess?.value = false
            openTaskAdapter?.notifyItemRemoved(homeViewModel?.removedItemIndex ?: 0)

            if(openTaskAdapter?.taskList?.isEmpty() == true) {
                displayDefaultScreen()
                return@observe
            }

        }

        homeViewModel?.isArchiveTaskSuccess?.observe(this) { isSuccess ->

            if(!isSuccess)
                return@observe

            openTaskAdapter?.notifyItemRemoved(homeViewModel?.archivedItemIndex ?: 0)

            if(openTaskAdapter?.taskList?.isEmpty() == true) {
                displayDefaultScreen()
                return@observe
            }

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

}