package com.example.todolist.ui.archived

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.todolist.R
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

    private val selectedTasks = mutableListOf<Task>()
    private var select: Boolean = false

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
        showSelectionTasks()
        setupSnackActions()

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
            binding.selectButton.visibility = if (tasks.isNotEmpty()) View.VISIBLE else View.GONE
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

    @SuppressLint("NotifyDataSetChanged")
    private fun showSelectionTasks() {
        binding.selectButton.setOnClickListener {
            if (select) {
                binding.snackBar.visibility = View.GONE
                binding.selectButton.text = getText(R.string.select_button_text)
                select = false
                taskAdapter.clearSelection()
                taskAdapter.setupSelectionMode(false)
            } else {
                binding.snackBar.visibility = View.VISIBLE
                binding.selectButton.text = getText(R.string.cancel_text)
                select = true
                taskAdapter.setupSelectionMode(true)
                taskAdapter.clearSelection()
            }
        }
    }

    private fun setupSnackActions() {
        binding.buttonDelete.setOnClickListener {

            if (selectedTasks.isNotEmpty()) {// Obtém as tarefas selecionadas
                selectedTasks.forEach {
                    archivedViewModel.deleteTask(it)
                }
                selectedTasks.clear()

                binding.snackBar.visibility = View.GONE
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
                    archivedViewModel.unarchiveTask(it)
                }
                selectedTasks.clear()

                binding.snackBar.visibility = View.GONE
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
                task.isSelected = !task.isSelected
                if (it.isSelected) selectedTasks.add(it) else selectedTasks.remove(it)
                taskAdapter.toggleSelection(task)
            } else {
                showDialog(it)
            }
        }
    }

    fun checkScreen () {
        val viewPager = requireActivity().findViewById<ViewPager2>(R.id.vp)

        if (viewPager.currentItem == 0 && select) {
            println("OK")
        } else {
            binding.snackBar.visibility = View.GONE
            binding.selectButton.text = getText(R.string.select_button_text)
            select = false
            taskAdapter.clearSelection()
            taskAdapter.setupSelectionMode(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding
    }
}
