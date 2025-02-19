package com.example.todo.archived

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.task.TaskSingleton
import com.example.todo.adapter.ArchivedTaskAdapter
import com.example.todo.databinding.FragmentArchivedBinding
import com.example.todo.main.MainActivity
import com.example.todo.main.MainViewModel
import com.example.todo.sharedpref.ToDoSharedPref
import com.example.todo.task.Task
import com.example.todo.task.TaskActionListener
import com.example.todo.task.TaskState
import java.util.Collections

class ArchivedFragment : Fragment() {

    private var binding: FragmentArchivedBinding? = null
    private var mainViewModel: MainViewModel? = null
    private var archivedViewModel: ArchivedViewModel? = null
    private var archivedTaskAdapter: ArchivedTaskAdapter? = null
    private var toDoSharedPref: ToDoSharedPref? = null

    companion object {
        const val TAG = "archived_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        archivedViewModel = ViewModelProvider(this)[ArchivedViewModel::class.java]
        toDoSharedPref = ToDoSharedPref.getInstance(context)

        setupObservers()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentArchivedBinding.inflate(layoutInflater)

        val helper = ItemTouchHelper(ArchivedItemTouchHelper(
            ItemTouchHelper.UP or
                    ItemTouchHelper.DOWN,
            0
        ))

        helper.attachToRecyclerView(binding?.recyclerViewTasks)

        return binding?.root

    }

    inner class ArchivedItemTouchHelper(
        dragDirs: Int,
        swipeDirs: Int
    ) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

        private val list: MutableList<Int> = emptyList<Int>().toMutableList()

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            val from = viewHolder.adapterPosition
            val to = target.adapterPosition

            archivedTaskAdapter?.taskList?.let { Collections.swap(it, from, to) }
            archivedTaskAdapter?.notifyItemMoved(from, to)

            TaskSingleton.archivedTaskList?.map { list.add(it.id) }
            toDoSharedPref?.saveArchivedTaskIdList(list)

            Log.d("SHARED_PREF", "TASK ID LIST: ${TaskSingleton.openTaskIdList}")

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    }

    private fun setupObservers() {

        mainViewModel?.getTasksState?.observe(this) { state ->

            when(state) {

                is TaskState.Success -> {

                    if(archivedTaskAdapter == null)
                        setupAdapter()

                    if(archivedTaskAdapter?.taskList?.isEmpty() == true) {
                        displayDefaultScreen()
                        return@observe
                    }

                    displayRecyclerViewScreen()

                }

                else -> {}

            }

        }

        mainViewModel?.archiveTaskState?.observe(this) { state ->

            when(state) {

                is TaskState.Success -> {

                    archivedTaskAdapter?.addNewTask(TaskSingleton.archivedTask)
                    binding?.recyclerViewTasks?.scrollToPosition(0)

                    if(archivedTaskAdapter?.taskList?.isEmpty() == true) {
                        displayDefaultScreen()
                        return@observe
                    }

                    displayRecyclerViewScreen()

                }

                else -> {}

            }

        }

        mainViewModel?.unarchiveTaskState?.observe(this) { state ->

            when(state) {

                is TaskState.Success -> {

                    archivedTaskAdapter?.notifyItemRemoved(mainViewModel?.unarchivedItemIndex ?: 0)
                    (activity as MainActivity).switchFromArchivedFragmentToHomeFragment()

                    Toast.makeText(context, getString(R.string.task_unarchived), Toast.LENGTH_LONG).show()

                    if(archivedTaskAdapter?.taskList?.isEmpty() == true) {
                        displayDefaultScreen()
                        return@observe
                    }

                    displayRecyclerViewScreen()

                }

                else -> {}

            }

        }

        archivedViewModel?.deleteTaskState?.observe(this) { state ->

            when(state) {

                is TaskState.Success -> {

                    archivedTaskAdapter?.notifyItemRemoved(archivedViewModel?.removedItemIndex ?: 0)

                    Toast.makeText(context, getString(R.string.task_deleted), Toast.LENGTH_LONG).show()

                    if(archivedTaskAdapter?.taskList?.isEmpty() == true) {
                        displayDefaultScreen()
                        return@observe
                    }

                    displayRecyclerViewScreen()

                }

                is TaskState.Error -> { Toast.makeText(context, getString(R.string.error_task_delete), Toast.LENGTH_LONG).show() }

                else -> {}

            }

        }

    }

    private fun setupAdapter() {

        archivedTaskAdapter = ArchivedTaskAdapter(
            TaskSingleton.archivedTaskList ?: emptyList<Task>().toMutableList(),
            object: TaskActionListener {
                override fun onUnarchiveTask(task: Task?) {
                    mainViewModel?.unarchiveTask(task)
                }

                override fun onDeleteTask(task: Task?) {
                    archivedViewModel?.deleteTask(task)
                }

                override fun onArchiveTask(task: Task?) {}

            })

        binding?.recyclerViewTasks?.adapter = archivedTaskAdapter
        binding?.recyclerViewTasks?.layoutManager = LinearLayoutManager(context)

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