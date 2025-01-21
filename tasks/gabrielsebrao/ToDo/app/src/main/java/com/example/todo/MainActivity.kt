package com.example.todo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.todo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var homeFragment: Fragment? = null
    private var archivedFragment: Fragment? = null
    private var addFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        homeFragment = HomeFragment()
        archivedFragment = ArchivedFragment()
        addFragment = AddFragment()

        replaceFragment(archivedFragment)
        replaceFragment(addFragment)
        replaceFragment(homeFragment)

        setupListeners()

    }

    private fun setupListeners() {

        setupBottomNavigationViewListeners()

    }

    private fun setupBottomNavigationViewListeners() {

        binding?.bottomNavigationView?.setOnItemSelectedListener { item ->

            when(item.itemId) {

                R.id.home -> {
                    showFragment(homeFragment)
                }

                R.id.archived -> {
                    showFragment(archivedFragment)
                }

                R.id.add -> {
                    showFragment(addFragment)
                }

            }

            true

        }

    }

    private fun replaceFragment(fragment: Fragment?) {

        if(fragment == null)
            return

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()

    }

    private fun showFragment(fragment: Fragment?) {

        if(fragment == null)
            return

        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_layout) ?: return
        val newFragment = supportFragmentManager.findFragmentByTag(fragment.javaClass.toString())

        if(newFragment == null) {
            replaceFragment(fragment)
            return
        }

        supportFragmentManager
            .beginTransaction()
            .hide(currentFragment)
            .commit()

        supportFragmentManager
            .beginTransaction()
            .show(fragment)
            .commit()

    }

}