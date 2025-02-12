package com.example.todolist.ui.archived

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.FragmentArchivedBinding
import com.example.todolist.ui.adapter.TaskAdapter
import com.example.todolist.ui.add.AddViewModelFactory
import com.example.todolist.ui.database.instance.DatabaseInstance
import com.example.todolist.ui.database.repository.TaskRepository

class ArchivedFragment : Fragment() {

    private lateinit var binding: FragmentArchivedBinding

    private lateinit var archivedViewModel: ArchivedViewModel
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArchivedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val database = DatabaseInstance.getDatabase(requireContext())
        val repository = TaskRepository(database.taskDao())

        val factory = ArchivedViewModelFactory(repository)
        archivedViewModel = ViewModelProvider(this, factory)[ArchivedViewModel::class.java]


        setupArchivedViewModel()
        setupRecyclerView()

        return root
    }

    private fun setupArchivedViewModel() {
        archivedViewModel.archivedTasksLiveData.observe(viewLifecycleOwner, Observer { tasks ->
            taskAdapter.updateTasks(tasks.toMutableList())
        })
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(mutableListOf(),
            onDeleteTask = { task -> archivedViewModel.deleteTask(task) }, {},
            onUnarchiveTask = { task -> archivedViewModel.unarchiveTask(task) })

        binding.recyclerViewFromArchived.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding
    }
}

