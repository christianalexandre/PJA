package com.example.todo.modules.main.fragments.home

import android.os.Bundle
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
import com.example.todo.utils.listener.TaskActionListener
import com.example.todo.utils.singleton.TaskSingleton
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.modules.main.MainViewModel
import com.example.todo.utils.dialog.TaskDialog
import com.example.todo.utils.listener.CardActionListener
import com.example.todo.utils.models.Task
import com.example.todo.utils.sharedpref.TaskIdListSharedPref
import java.util.Collections

class HomeFragment : Fragment(), TaskActionListener {

    private var binding: FragmentHomeBinding? = null
    private var mainViewModel: MainViewModel? = null
    private var homeViewModel: HomeViewModel? = null
    private var homeTaskAdapter: HomeTaskAdapter? = null
    private var taskIdListSharedPref: TaskIdListSharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        taskIdListSharedPref = TaskIdListSharedPref.getInstance(context)

        setupObservers()

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

    override fun onDeleteTask(task: Task?) {
        homeViewModel?.deleteTask(task)
    }

    override fun onSecondAction(task: Task?) {
        mainViewModel?.archiveTask(task)
    }

    inner class HomeItemTouchHelper(
        dragDirs: Int,
        swipeDirs: Int
    ) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            val from = viewHolder.adapterPosition
            val to = target.adapterPosition

            homeTaskAdapter?.taskList?.let { Collections.swap(it, from, to) }
            homeTaskAdapter?.notifyItemMoved(from, to)

            val list: MutableList<Int> = emptyList<Int>().toMutableList()
            TaskSingleton.openTaskList?.map { list.add(it.id) }
            taskIdListSharedPref?.saveOpenTaskIdList(list)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    }

    private fun setupObservers() {

        homeViewModel?.deleteTaskSuccess?.observe(this) { isSuccess ->

            when(isSuccess) {

                true -> {

                    homeTaskAdapter?.removeTask(homeViewModel?.removedItemIndex ?: 0)

                    Toast.makeText(context, getString(R.string.task_deleted), Toast.LENGTH_SHORT).show()

                    if(homeTaskAdapter?.taskList?.isEmpty() == true) {
                        displayDefaultScreen()
                        return@observe
                    }

                    displayRecyclerViewScreen()

                }

                false -> { Toast.makeText(context, getString(R.string.error_task_delete), Toast.LENGTH_SHORT).show() }

                else -> {}

            }

        }

    }

    private fun setupAdapter() {

        homeTaskAdapter = HomeTaskAdapter(
            TaskSingleton.openTaskList ?: emptyList<Task>().toMutableList(),
            object : CardActionListener {
                override fun onCheckClicked(task: Task?) {
                    if(parentFragmentManager.fragments.any { it.tag == TaskDialog.TAG })
                        return

                    parentFragmentManager.findFragmentByTag(TaskDialog.TAG)?.let { fragment ->
                        (fragment as? TaskDialog)?.dismiss()
                    }

                    val dialog = TaskDialog.newInstance(task, this@HomeFragment, true)
                    dialog.show(parentFragmentManager, TaskDialog.TAG)
                }
            })

        binding?.recyclerHomeViewTasks?.adapter = homeTaskAdapter
        binding?.recyclerHomeViewTasks?.layoutManager = LinearLayoutManager(context)

    }

    private fun displayRecyclerViewScreen() {

        binding?.defaultHomeEmptyTaskList?.visibility = View.GONE
        binding?.recyclerHomeViewTasks?.visibility = View.VISIBLE

    }

    private fun displayDefaultScreen() {

        binding?.defaultHomeEmptyTaskList?.visibility = View.VISIBLE
        binding?.recyclerHomeViewTasks?.visibility = View.GONE

    }

    fun onGetTasks() {

        if(homeTaskAdapter == null)
            setupAdapter()

        if(homeTaskAdapter?.taskList?.isEmpty() == true) {
            displayDefaultScreen()
            return
        }

        displayRecyclerViewScreen()

    }

    fun onAddTask() {

        if((TaskSingleton.openTaskList?.size ?: return) > 0)
            displayRecyclerViewScreen()

        homeTaskAdapter?.addNewTask()

        binding?.recyclerHomeViewTasks?.scrollToPosition(0)

    }

    fun onArchiveTask() {

        homeTaskAdapter?.removeTask(mainViewModel?.archivedItemIndex ?: 0)

        if(homeTaskAdapter?.taskList?.isEmpty() == true) {
            displayDefaultScreen()
            return
        }

        displayRecyclerViewScreen()

    }

    fun onUnarchiveTask() {

        homeTaskAdapter?.addNewTask()
        binding?.recyclerHomeViewTasks?.scrollToPosition(0)

        displayRecyclerViewScreen()

    }

}