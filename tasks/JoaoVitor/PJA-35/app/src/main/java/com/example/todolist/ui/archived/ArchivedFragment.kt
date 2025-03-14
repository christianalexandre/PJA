package com.example.todolist.ui.archived

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.FragmentArchivedBinding
import com.example.todolist.ui.dialog.DialogOrigin
import com.example.todolist.ui.dialog.CustomDialogFragment
import com.example.todolist.ui.adapter.TaskAdapter
import com.example.todolist.ui.adapter.TaskListener
import com.example.todolist.ui.database.instance.DatabaseInstance
import com.example.todolist.ui.database.model.Task
import com.example.todolist.ui.database.repository.TaskRepository

class ArchivedFragment : Fragment(), TaskListener {

    private lateinit var binding: FragmentArchivedBinding
    private lateinit var archivedViewModel: ArchivedViewModel
    private lateinit var taskAdapter: TaskAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentArchivedBinding.inflate(inflater, container, false)

        val database = DatabaseInstance.getDatabase(requireContext())
        val repository = TaskRepository(database.taskDao())
        val factory = ArchivedViewModelFactory(repository)

        archivedViewModel = ViewModelProvider(this, factory)[ArchivedViewModel::class.java]

        setupRecyclerView()
        observeViewModel()

        return binding.root
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(mutableListOf(), this, false)
        binding.recyclerViewFromArchived.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeViewModel() {
        archivedViewModel.archivedTasksLiveData.observe(viewLifecycleOwner) { tasks ->
            taskAdapter.updateTasks(tasks.toMutableList())

            binding.notTaskFromArchived.visibility = if (tasks.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun showDialog(task: Task) {
        if (parentFragmentManager.findFragmentByTag("CustomDialogArchivedFragment") == null) {
            val listener = object : CustomDialogFragment.DialogListener {
                override fun onFirstPressed() {
                    archivedViewModel.unarchiveTask(task)
                }

                override fun onSecondPressed() {
                    archivedViewModel.deleteTask(task)
                }
            }
            CustomDialogFragment.checkShowDialog(parentFragmentManager, DialogOrigin.ARCHIEVE_CARD_CHECK, listener)
        }
    }

    override fun onCheckPressed(task: Task?) {
        task?.let { showDialog(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding
    }
}
