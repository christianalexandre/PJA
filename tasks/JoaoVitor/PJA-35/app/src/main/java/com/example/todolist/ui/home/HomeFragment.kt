package com.example.todolist.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.databinding.FragmentHomeBinding
import com.example.todolist.ui.dialog.DialogOrigin
import com.example.todolist.ui.adapter.TaskAdapter
import com.example.todolist.ui.adapter.TaskListener
import com.example.todolist.ui.dialog.CustomDialogFragment
import com.example.todolist.ui.database.instance.DatabaseInstance
import com.example.todolist.ui.database.model.Task
import com.example.todolist.ui.database.repository.TaskRepository

class HomeFragment : Fragment(), TaskListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var taskAdapter: TaskAdapter

    private val selectedTasks = mutableListOf<Task>()
    private var select: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val database = DatabaseInstance.getDatabase(requireContext())
        val repository = TaskRepository(database.taskDao())
        val factory = HomeViewModelFactory(repository)

        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        setupRecyclerView()
        observeViewModel()
        showSelectionTasks()
        setupToolbarActions()

        return binding.root
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(mutableListOf(), this, true)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeViewModel() {
        homeViewModel.tasksLiveData.observe(viewLifecycleOwner) { tasks ->
            taskAdapter.updateTasks(tasks)

            binding.notTaskFromHome.visibility = if (tasks.isEmpty()) View.VISIBLE else View.GONE
            binding.selectButton.visibility = if (tasks.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun showDialog(task: Task) {
        if (parentFragmentManager.findFragmentByTag("CustomDialogFragment") == null) {
            val listener = object : CustomDialogFragment.DialogListener {
                override fun onFirstPressed() {
                    homeViewModel.archiveTask(task)
                }

                override fun onSecondPressed() {
                    homeViewModel.deleteTask(task)
                }
            }
            CustomDialogFragment.checkShowDialog(parentFragmentManager, DialogOrigin.HOME_CARD_CHECK, listener)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSelectionTasks() {
        binding.selectButton.setOnClickListener {
            if (select) {
                binding.toolbarBottom.visibility = View.GONE
                binding.selectButton.text = getText(R.string.select_button_text)
                select = false
                taskAdapter.clearSelection()
            } else {
                binding.toolbarBottom.visibility = View.VISIBLE
                binding.selectButton.text = getText(R.string.cancel_text)
                select = true
            }
        }
    }

    private fun setupToolbarActions() {
        binding.buttonDelete.setOnClickListener {

            if (selectedTasks.isNotEmpty()) {// Obtém as tarefas selecionadas
                selectedTasks.forEach {
                    homeViewModel.deleteTask(it)
                }
                selectedTasks.clear()

                binding.toolbarBottom.visibility = View.GONE
                binding.selectButton.text = getText(R.string.select_button_text)
                select = false
            } else {
                val notification = Toast.makeText(requireContext(), "Nenhuma tarefa selecionada!", Toast.LENGTH_SHORT)
                notification.setGravity(Gravity.CENTER, 50, 50)
                notification.show()
            }
        }

        binding.buttonArchive.setOnClickListener {
            if (selectedTasks.isNotEmpty()) {
                selectedTasks.forEach {
                    homeViewModel.archiveTask(it)
                }
                selectedTasks.clear()

                binding.toolbarBottom.visibility = View.GONE
                binding.selectButton.text = getText(R.string.select_button_text)
                select = false
            } else {
                val notification = Toast.makeText(requireContext(), "Nenhuma tarefa selecionada!", Toast.LENGTH_SHORT)
                notification.setGravity(Gravity.CENTER, 50, 50)
                notification.show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCheckPressed(task: Task?) {
        task?.let {
            if (select) {
                if (it.isSelected) selectedTasks.add(it) else selectedTasks.remove(it)
                taskAdapter.toggleSelection(task)
            } else {
                showDialog(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding
    }
}
