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
import com.example.todo.task.TaskActionListener
import com.example.todo.task.TaskSingleton
import com.example.todo.adapter.OpenTaskAdapter
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.main.MainActivity
import com.example.todo.main.MainViewModel
import com.example.todo.room.DataBase
import com.example.todo.task.Task
import com.example.todo.task.TaskDao
import com.example.todo.sharedpref.ToDoSharedPref
import java.util.Collections

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private var mainViewModel: MainViewModel? = null
    private var homeViewModel: HomeViewModel? = null
    private var db: DataBase? = null
    private var taskDao: TaskDao? = null
    private var openTaskAdapter: OpenTaskAdapter? = null
    private var toDoSharedPref: ToDoSharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        db = DataBase.getInstance(context)
        taskDao = db?.taskDao()
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        toDoSharedPref = ToDoSharedPref.getInstance(context)

        setupObservers()

        mainViewModel?.getAllTasks()

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

        helper.attachToRecyclerView(binding?.recyclerHomeViewTasks)

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

        binding?.recyclerHomeViewTasks?.scrollToPosition(0)

    }

    private fun setupObservers() {

        mainViewModel?.isGetAllTasksSuccess?.observe(this) { isSuccess ->

            if(!isSuccess)
                return@observe

            if(openTaskAdapter == null) {

                openTaskAdapter = OpenTaskAdapter(
                    TaskSingleton.openTaskList ?: emptyList<Task>().toMutableList(),
                    object : TaskActionListener {
                        override fun onArchiveTask(task: Task?) {
                            mainViewModel?.archiveTask(task)
                        }

                        override fun onDeleteTask(task: Task?) {
                            homeViewModel?.deleteTask(task)
                        }

                        override fun onUnarchiveTask(task: Task?) {}
                    })

                binding?.recyclerHomeViewTasks?.adapter = openTaskAdapter
                binding?.recyclerHomeViewTasks?.layoutManager = LinearLayoutManager(context)
            }

            mainViewModel?.isGetAllTasksSuccess?.value = false

            if(openTaskAdapter?.taskList?.isEmpty() == true) {
                displayDefaultScreen()
                return@observe
            }

            displayRecyclerViewScreen()

        }

        homeViewModel?.isDeleteTaskSuccess?.observe(this) { isSuccess ->

            if(!isSuccess)
                return@observe

            openTaskAdapter?.notifyItemRemoved(homeViewModel?.removedItemIndex ?: 0)
            if(openTaskAdapter?.taskList?.isEmpty() == true) {
                displayDefaultScreen()
                return@observe
            }

            homeViewModel?.isDeleteTaskSuccess?.value = false


        }

        mainViewModel?.isArchiveTaskSuccess?.observe(this) { isSuccess ->

            if(!isSuccess)
                return@observe

            openTaskAdapter?.notifyItemRemoved(mainViewModel?.archivedItemIndex ?: 0)
            (activity as MainActivity).switchFromHomeFragmentToArchivedFragment()

            if(openTaskAdapter?.taskList?.isEmpty() == true) {
                displayDefaultScreen()
                return@observe
            }

        }

    }

    private fun displayRecyclerViewScreen() {

        binding?.defaultHomeEmptyTaskList?.visibility = View.GONE
        binding?.recyclerHomeViewTasks?.visibility = View.VISIBLE

    }

    private fun displayDefaultScreen() {

        binding?.defaultHomeEmptyTaskList?.visibility = View.VISIBLE
        binding?.recyclerHomeViewTasks?.visibility = View.GONE

    }

}