package com.example.todo.modules.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.todo.R
import com.example.todo.modules.main.fragments.add.AddFragment
import com.example.todo.modules.main.fragments.archived.ArchivedFragment
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.modules.main.fragments.home.HomeFragment
import com.example.todo.utils.task.TaskState

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var mainViewModel: MainViewModel? = null
    private var homeFragment: Fragment? = null
    private var archivedFragment: Fragment? = null
    private var addFragment: Fragment? = null
    private var viewPager: ViewPager2? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setupFragments()
        setupListeners()
        setupObservers()

    }

    private fun setupFragments() {

        homeFragment = HomeFragment()
        archivedFragment = ArchivedFragment()
        addFragment = AddFragment()

        viewPager = binding?.viewPager
        viewPager?.adapter = MainViewPagerAdapter(this, listOf(homeFragment, archivedFragment, addFragment))

        viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding?.bottomNavigationView?.menu?.getItem(position)?.isChecked = true
            }
        })

    }

    private fun setupListeners() {

        setupBottomNavigationViewListeners()

    }

    private fun setupBottomNavigationViewListeners() {

        binding?.bottomNavigationView?.setOnItemSelectedListener { item ->

            when(item.itemId) {

                R.id.home -> viewPager?.currentItem = 0

                R.id.archived -> viewPager?.currentItem = 1

                R.id.add -> viewPager?.currentItem = 2

            }

            true

        }

    }

    private fun setupObservers() {

        mainViewModel?.getTasksState?.observe(this) { state ->
            when(state) {

                is TaskState.Success -> {

                    (homeFragment as? HomeFragment)?.onGetTasks()
                    (archivedFragment as? ArchivedFragment)?.onGetTasks()

                }

                is TaskState.Error -> {
                    Toast.makeText(this, getString(R.string.error_task_get), Toast.LENGTH_LONG).show()
                }

                else -> {}

            }
        }

        mainViewModel?.addTaskState?.observe(this) { state ->
            when(state) {

                is TaskState.Success -> {

                    (addFragment as? AddFragment)?.onAddTask()
                    binding?.bottomNavigationView?.selectedItemId = R.id.home
                    (homeFragment as? HomeFragment)?.onAddTask()
                    Toast.makeText(this, getString(R.string.task_created), Toast.LENGTH_SHORT).show()

                }

                is TaskState.Error -> {
                    Toast.makeText(this, getString(R.string.error_task_created), Toast.LENGTH_SHORT).show()
                }

                else -> {}

            }
        }

        mainViewModel?.archiveTaskState?.observe(this) { state ->
            when(state) {

                is TaskState.Success -> {

                    (homeFragment as? HomeFragment)?.onArchiveTask()
                    binding?.bottomNavigationView?.selectedItemId = R.id.archived
                    (archivedFragment as? ArchivedFragment)?.onArchiveTask()
                    Toast.makeText(this, getString(R.string.task_archived), Toast.LENGTH_LONG).show()

                }

                is TaskState.Error -> {
                    Toast.makeText(this, getString(R.string.error_task_archive), Toast.LENGTH_LONG).show()
                }

                else -> {}

            }
        }

        mainViewModel?.unarchiveTaskState?.observe(this) { state ->
            when(state) {

                is TaskState.Success -> {

                    (archivedFragment as? ArchivedFragment)?.onUnarchiveTask()
                    binding?.bottomNavigationView?.selectedItemId = R.id.home
                    (homeFragment as? HomeFragment)?.onUnarchiveTask()
                    Toast.makeText(this, getString(R.string.task_unarchived), Toast.LENGTH_LONG).show()

                }

                is TaskState.Error -> {
                    Toast.makeText(this, R.string.error_task_unarchive, Toast.LENGTH_LONG).show()
                }

                else -> {}

            }
        }

    }

}