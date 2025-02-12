package com.example.todolist.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.FragmentHomeBinding
import com.example.todolist.ui.adapter.TaskAdapter
import com.example.todolist.ui.database.instance.DatabaseInstance
import com.example.todolist.ui.database.repository.TaskRepository

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var taskAdapter: TaskAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root

        setupHomeViewModel()
        setupRecyclerView()

        return root
    }

    private fun setupHomeViewModel() {
        val database = DatabaseInstance.getDatabase(requireContext())
        val repository = TaskRepository(database.taskDao())

        val factory = HomeViewModelFactory(repository)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        homeViewModel.tasksLiveData.observe(viewLifecycleOwner, Observer { tasks ->
            taskAdapter.updateTasks(tasks.toMutableList())
        })
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(mutableListOf(),
            onDeleteTask = { task -> homeViewModel.deleteTask(task) },
            onArchiveTask = { task -> homeViewModel.archiveTask(task) }, {}
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
