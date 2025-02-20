package com.example.todo.modules.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
    private var currentFragmentTag: String = HomeFragment.TAG

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setupFragments()
        setupListeners()
        setupObservers()

    }

    override fun onResume() {

        super.onResume()

        val activeFragment = supportFragmentManager.fragments.find { !it.isHidden }
        currentFragmentTag = activeFragment?.tag ?: HomeFragment.TAG

    }

    private fun setupFragments() {

        homeFragment = supportFragmentManager.findFragmentByTag(HomeFragment.TAG) ?: HomeFragment()
        archivedFragment = supportFragmentManager.findFragmentByTag(ArchivedFragment.TAG) ?: ArchivedFragment()
        addFragment = supportFragmentManager.findFragmentByTag(AddFragment.TAG) ?: AddFragment()

        if(supportFragmentManager.findFragmentByTag(AddFragment.TAG) == null) {
            createFragment(addFragment, AddFragment.TAG)
            hideFragment(addFragment)
        }

        if(supportFragmentManager.findFragmentByTag(ArchivedFragment.TAG) == null) {
            createFragment(archivedFragment, ArchivedFragment.TAG)
            hideFragment(archivedFragment)
        }

        if(supportFragmentManager.findFragmentByTag(HomeFragment.TAG) == null)
            createFragment(homeFragment, HomeFragment.TAG)

    }

    private fun setupListeners() {

        setupBottomNavigationViewListeners()

    }

    private fun setupBottomNavigationViewListeners() {

        var fragment: Fragment?

        binding?.bottomNavigationView?.setOnItemSelectedListener { item ->

            fragment = when(item.itemId) {

                R.id.home -> homeFragment

                R.id.archived -> archivedFragment

                R.id.add -> addFragment

                else -> null
            }

            if(fragment?.tag == currentFragmentTag)
                return@setOnItemSelectedListener true

            hideFragment(supportFragmentManager.findFragmentByTag(currentFragmentTag))

            showFragment(fragment)
            currentFragmentTag = when(fragment) {
                is HomeFragment -> HomeFragment.TAG
                is ArchivedFragment -> ArchivedFragment.TAG
                is AddFragment -> AddFragment.TAG
                else -> currentFragmentTag
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
                    switchFromAddFragmentToHomeFragment()
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
                    switchFromHomeFragmentToArchivedFragment()
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
                    switchFromArchivedFragmentToHomeFragment()
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

    private fun createFragment(fragment: Fragment?, fragmentTag: String) {

        if(fragment == null)
            return

        supportFragmentManager
            .beginTransaction()
            .add(R.id.frame_layout, fragment, fragmentTag)
            .commit()

    }

    private fun showFragment(fragment: Fragment?) {

        if(fragment == null)
            return

        supportFragmentManager
            .beginTransaction()
            .show(fragment)
            .commit()

    }

    private fun hideFragment(fragment: Fragment?) {

        if(fragment == null)
            return

        supportFragmentManager
            .beginTransaction()
            .hide(fragment)
            .commit()

    }

    private fun switchFromAddFragmentToHomeFragment() {

        hideFragment(supportFragmentManager.findFragmentByTag(AddFragment.TAG))
        binding?.bottomNavigationView?.selectedItemId = R.id.home
        currentFragmentTag = HomeFragment.TAG

    }

    private fun switchFromHomeFragmentToArchivedFragment() {

        hideFragment(supportFragmentManager.findFragmentByTag(HomeFragment.TAG))
        binding?.bottomNavigationView?.selectedItemId = R.id.archived
        currentFragmentTag = ArchivedFragment.TAG

    }

    private fun switchFromArchivedFragmentToHomeFragment() {

        hideFragment(supportFragmentManager.findFragmentByTag(HomeFragment.TAG))
        binding?.bottomNavigationView?.selectedItemId = R.id.home
        currentFragmentTag = HomeFragment.TAG

    }

}