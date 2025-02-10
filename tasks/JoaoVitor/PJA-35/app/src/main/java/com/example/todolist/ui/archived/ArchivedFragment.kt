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
import com.example.todolist.ui.database.db.AppDatabase
import com.example.todolist.ui.database.instance.DatabaseInstance

class ArchivedFragment : Fragment() {

    private var _binding: FragmentArchivedBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: AppDatabase
    private lateinit var archivedViewModel: ArchivedViewModel
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArchivedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        database = DatabaseInstance.getDatabase(requireContext())

        setupArchivedViewModel()
        setupRecyclerView()

        return root
    }

    private fun setupArchivedViewModel() {
        val factory = ArchivedViewModelFactory(database.taskDao())
        archivedViewModel = ViewModelProvider(this, factory)[ArchivedViewModel::class.java]

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
        _binding = null
    }
}

