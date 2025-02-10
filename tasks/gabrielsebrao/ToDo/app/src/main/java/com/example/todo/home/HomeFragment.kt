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
import com.example.todo.sharedpref.ToDoSharedPref
import java.util.Collections

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private var homeViewModel: HomeViewModel? = null
    private var db: DataBase? = null
    private var taskDao: TaskDao? = null
    private var taskAdapter: TaskAdapter? = null
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

    inner class ItemTouchHelper(dragDirs: Int, swipeDirs: Int) : androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean {
            val from = viewHolder.adapterPosition
            val to = target.adapterPosition

            taskAdapter?.taskList?.let { Collections.swap(it, from, to) }
            taskAdapter?.notifyItemMoved(from, to)

            val list: MutableList<Int> = emptyList<Int>().toMutableList()
            TaskSingleton.openTaskList?.map { list.add(it.id) }
            toDoSharedPref?.saveList(list)

            Log.d("SHARED_PREF", "TASK ID LIST: ${toDoSharedPref?.idList}")

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
            0
        ))

        helper.attachToRecyclerView(binding?.recyclerViewTasks)

        return binding?.root

    }

    fun onShown() {

        if((TaskSingleton.openTaskList?.size ?: return) > 0)
            displayRecyclerViewScreen()

        taskAdapter?.addNewTask(TaskSingleton.newTask)
        TaskSingleton.newTask = null

        binding?.recyclerViewTasks?.scrollToPosition(0)

    }

    private fun setupObservers() {

        homeViewModel?.isGetAllTasksSuccess?.observe(this) { isSuccess ->

            if(!isSuccess)
                return@observe

            TaskSingleton.openTaskList = TaskSingleton.openTaskList?.sortedBy { task ->
                toDoSharedPref?.idList?.indexOf(task.id)
            }?.toMutableList()

            if(taskAdapter == null) {
                taskAdapter = TaskAdapter(TaskSingleton.openTaskList ?: emptyList<Task>().toMutableList(), homeViewModel)
                binding?.recyclerViewTasks?.adapter = taskAdapter
                binding?.recyclerViewTasks?.layoutManager = LinearLayoutManager(context)
            }

            Log.e("ROOM_DEBUG", "${TaskSingleton.openTaskList}")

            homeViewModel?.isGetAllTasksSuccess?.value = false

            if(taskAdapter?.taskList?.isEmpty() == true) {
                displayDefaultScreen()
                return@observe
            }

            displayRecyclerViewScreen()

        }

        var removedTask: Task
        var removedItemIndex: Int

        homeViewModel?.isDeleteTaskSuccess?.observe(this) { isSuccess ->

            if(!isSuccess)
                return@observe


            removedTask = TaskSingleton.openTaskList?.find { it.id == TaskSingleton.deletedTaskId } ?: return@observe
            removedItemIndex = TaskSingleton.openTaskList?.indexOf(removedTask) ?: return@observe

            TaskSingleton.openTaskList?.remove(removedTask)
            taskAdapter?.notifyItemRemoved(removedItemIndex)

            homeViewModel?.isDeleteTaskSuccess?.value = false

            if(taskAdapter?.taskList?.isEmpty() == true) {
                displayDefaultScreen()
                return@observe
            }

        }

        var archivedTask: Task
        var archivedItemIndex: Int

        homeViewModel?.isArchiveTaskSuccess?.observe(this) { isSuccess ->

            if(!isSuccess)
                return@observe

            archivedTask = TaskSingleton.openTaskList?.find { it.isArchived } ?: return@observe
            archivedItemIndex = TaskSingleton.openTaskList?.indexOf(archivedTask) ?: return@observe

            TaskSingleton.openTaskList?.remove(archivedTask)
            taskAdapter?.notifyItemRemoved(archivedItemIndex)

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