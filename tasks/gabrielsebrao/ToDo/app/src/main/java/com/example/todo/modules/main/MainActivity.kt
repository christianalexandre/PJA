package com.example.todo.modules.main

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
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
import com.example.todo.modules.splash.SplashActivity

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var mainViewModel: MainViewModel? = null
    private var homeFragment: Fragment? = null
    private var archivedFragment: Fragment? = null
    private var addFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        if(savedInstanceState != null) {
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setupView()
        setupFragments()
        setupListeners()
        setupObservers()

    }

    private fun setupView() {

        setSupportActionBar(binding?.toolBar)
        supportActionBar?.title = getString(R.string.home)

    }

    private fun setupFragments() {

        homeFragment = HomeFragment()
        archivedFragment = ArchivedFragment()
        addFragment = AddFragment()

        binding?.viewPager?.adapter = MainViewPagerAdapter(this, listOf(homeFragment, archivedFragment, addFragment))
        binding?.viewPager?.offscreenPageLimit = 2

    }

    private fun setupListeners() {

        setupViewPagerListeners()
        setupBottomNavigationViewListeners()

    }

    private fun setupViewPagerListeners() {

        binding?.viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                super.onPageSelected(position)

                with(binding?.bottomNavigationView) {
                    this?.selectedItemId = this?.menu?.getItem(position)?.itemId ?: R.id.home
                }

                (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(binding?.root?.windowToken, 0)

            }
        })

    }

    private fun setupBottomNavigationViewListeners() {

        binding?.bottomNavigationView?.setOnItemSelectedListener { item ->

            when(item.itemId) {

                R.id.home -> {
                    binding?.viewPager?.currentItem = 0
                    supportActionBar?.title = getString(R.string.home)
                }

                R.id.archived -> {
                    binding?.viewPager?.currentItem = 1
                    supportActionBar?.title = getString(R.string.archived)
                }

                R.id.add -> {
                    binding?.viewPager?.currentItem = 2
                    supportActionBar?.title = getString(R.string.add)
                }

            }

            true

        }

    }

    private fun setupObservers() {

        mainViewModel?.addTaskSuccess?.observe(this) { isSuccess ->
            when(isSuccess) {

                true -> {

                    (addFragment as? AddFragment)?.onAddTask()
                    binding?.bottomNavigationView?.selectedItemId = R.id.home
                    (homeFragment as? HomeFragment)?.onAddTask()
                    Toast.makeText(this, getString(R.string.task_created), Toast.LENGTH_SHORT).show()

                }

                false -> {
                    Toast.makeText(this, getString(R.string.error_task_created), Toast.LENGTH_SHORT).show()
                }

                else -> {}

            }
        }

        mainViewModel?.archiveTaskSuccess?.observe(this) { isSuccess ->
            when(isSuccess) {

                true -> {

                    (homeFragment as? HomeFragment)?.onArchiveTask()
                    binding?.bottomNavigationView?.selectedItemId = R.id.archived
                    (archivedFragment as? ArchivedFragment)?.onArchiveTask()
                    Toast.makeText(this, getString(R.string.task_archived), Toast.LENGTH_SHORT).show()

                }

                false -> {
                    Toast.makeText(this, getString(R.string.error_task_archive), Toast.LENGTH_SHORT).show()
                }

                else -> {}

            }
        }

        mainViewModel?.unarchiveTaskSuccess?.observe(this) { isSuccess ->
            when(isSuccess) {

                true -> {

                    (archivedFragment as? ArchivedFragment)?.onUnarchiveTask()
                    binding?.bottomNavigationView?.selectedItemId = R.id.home
                    (homeFragment as? HomeFragment)?.onUnarchiveTask()
                    Toast.makeText(this, getString(R.string.task_unarchived), Toast.LENGTH_SHORT).show()

                }

                false -> {
                    Toast.makeText(this, R.string.error_task_unarchive, Toast.LENGTH_SHORT).show()
                }

                else -> {}

            }
        }

    }

}