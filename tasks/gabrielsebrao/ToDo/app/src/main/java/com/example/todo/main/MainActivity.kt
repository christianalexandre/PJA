package com.example.todo.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todo.R
import com.example.todo.add.AddFragment
import com.example.todo.archived.ArchivedFragment
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.home.HomeFragment

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

        if(fragment is HomeFragment)
            fragment.onShown()

    }

    private fun hideFragment(fragment: Fragment?) {

        if(fragment == null)
            return

        supportFragmentManager
            .beginTransaction()
            .hide(fragment)
            .commit()

    }

    fun switchFromAddFragmentToHomeFragment() {

        hideFragment(supportFragmentManager.findFragmentByTag(AddFragment.TAG))
        binding?.bottomNavigationView?.selectedItemId = R.id.home
        currentFragmentTag = HomeFragment.TAG

    }

    fun switchFromHomeFragmentToArchivedFragment() {

        hideFragment(supportFragmentManager.findFragmentByTag(HomeFragment.TAG))
        binding?.bottomNavigationView?.selectedItemId = R.id.archived
        currentFragmentTag = ArchivedFragment.TAG

    }

    fun switchFromArchivedFragmentToHomeFragment() {

        hideFragment(supportFragmentManager.findFragmentByTag(HomeFragment.TAG))
        binding?.bottomNavigationView?.selectedItemId = R.id.home
        currentFragmentTag = HomeFragment.TAG

    }

}