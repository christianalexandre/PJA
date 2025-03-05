package com.example.todo.modules.main.fragments.archived

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
import com.example.todo.utils.singleton.TaskSingleton
import com.example.todo.databinding.FragmentArchivedBinding
import com.example.todo.modules.main.MainViewModel
import com.example.todo.utils.dialog.ImageDialog
import com.example.todo.utils.dialog.TaskDialog
import com.example.todo.utils.listener.CardActionListener
import com.example.todo.utils.sharedpref.TaskIdListSharedPref
import com.example.todo.utils.models.Task
import com.example.todo.utils.listener.TaskActionListener
import java.util.Collections

class ArchivedFragment : Fragment(), TaskActionListener {

    private var binding: FragmentArchivedBinding? = null
    private var mainViewModel: MainViewModel? = null
    private var archivedViewModel: ArchivedViewModel? = null
    private var archivedTaskAdapter: ArchivedTaskAdapter? = null
    private var taskIdListSharedPref: TaskIdListSharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        archivedViewModel = ViewModelProvider(this)[ArchivedViewModel::class.java]
        taskIdListSharedPref = TaskIdListSharedPref.getInstance(context)

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

    override fun onDeleteTask(task: Task?) {
        archivedViewModel?.deleteTask(task)
    }

    override fun onSecondAction(task: Task?) {
        mainViewModel?.unarchiveTask(task)
    }

    inner class ArchivedItemTouchHelper(
        dragDirs: Int,
        swipeDirs: Int
    ) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            val from = viewHolder.adapterPosition
            val to = target.adapterPosition

            archivedTaskAdapter?.taskList?.let { Collections.swap(it, from, to) }
            archivedTaskAdapter?.notifyItemMoved(from, to)

            val list: MutableList<Int> = emptyList<Int>().toMutableList()
            TaskSingleton.archivedTaskList?.map { list.add(it.id) }
            taskIdListSharedPref?.saveArchivedTaskIdList(list)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    }

    private fun setupObservers() {

        archivedViewModel?.deleteTaskSuccess?.observe(this) { state ->

            when(state) {

                true -> {

                    archivedTaskAdapter?.removeTask(archivedViewModel?.removedItemIndex ?: 0)

                    Toast.makeText(context, getString(R.string.task_deleted), Toast.LENGTH_SHORT).show()

                    if(archivedTaskAdapter?.taskList?.isEmpty() == true) {
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

        archivedTaskAdapter = ArchivedTaskAdapter(
            TaskSingleton.archivedTaskList ?: emptyList<Task>().toMutableList(),
            object: CardActionListener {
                override fun onCheckClicked(task: Task?) {
                    if(parentFragmentManager.fragments.any { it.tag == TaskDialog.TAG })
                        return

                    val dialog = TaskDialog.newInstance(task, this@ArchivedFragment, false)
                    dialog.show(parentFragmentManager, TaskDialog.TAG)
                }

                override fun onImageCLicked(task: Task?) {
                    val dialog = ImageDialog.newInstance(task?.image)
                    dialog.show(parentFragmentManager, TaskDialog.TAG)
                }
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

    fun onGetTasks() {

        if(archivedTaskAdapter == null)
            setupAdapter()

        if(archivedTaskAdapter?.taskList?.isEmpty() == true) {
            displayDefaultScreen()
            return
        }

        displayRecyclerViewScreen()

    }

    fun onArchiveTask() {

        archivedTaskAdapter?.addNewTask()
        binding?.recyclerViewTasks?.scrollToPosition(0)

        if(archivedTaskAdapter?.taskList?.isEmpty() == true) {
            displayDefaultScreen()
            return
        }

        displayRecyclerViewScreen()

    }

    fun onUnarchiveTask() {

        archivedTaskAdapter?.removeTask(mainViewModel?.unarchivedItemIndex ?: 0)

        if(archivedTaskAdapter?.taskList?.isEmpty() == true) {
            displayDefaultScreen()
            return
        }

        displayRecyclerViewScreen()

    }

}