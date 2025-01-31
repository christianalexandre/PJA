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
import com.example.todolist.ui.database.db.AppDatabase
import com.example.todolist.ui.database.instance.DatabaseInstance

class HomeFragment : Fragment() {

    private lateinit var database: AppDatabase
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

        database = DatabaseInstance.getDatabase(requireContext())

        setupHomeViewModel()
        setupRecyclerView()

        return root
    }

    private fun setupHomeViewModel() {
        val factory = HomeViewModelFactory(database.taskDao())
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        // Observar mudanças na lista de tarefas
        homeViewModel.tasksLiveData.observe(viewLifecycleOwner, Observer { tasks ->
            taskAdapter.updateTasks(tasks)
        })
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(emptyList())

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
