package com.example.todo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.todo.databinding.ActivityMainBinding

private const val DEFAULT = "default"
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var homeFragment: Fragment? = null
    private var archivedFragment: Fragment? = null
    private var addFragment: Fragment? = null
    private var fragmentMap: Map<String, String>? = null
    private var currentFragmentTag: String = DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        homeFragment = HomeFragment()
        archivedFragment = ArchivedFragment()
        addFragment = AddFragment()

        fragmentMap = mapOf(
            Pair(addFragment?.javaClass.toString(), "add_fragment"),
            Pair(archivedFragment?.javaClass.toString(), "archived_fragment"),
            Pair(homeFragment?.javaClass.toString(), "home_fragment")
        )

        addFragment(addFragment, fragmentMap?.get(addFragment?.javaClass.toString()) ?: DEFAULT)
        addFragment(archivedFragment, fragmentMap?.get(archivedFragment?.javaClass.toString()) ?: DEFAULT)
        addFragment(homeFragment, fragmentMap?.get(homeFragment?.javaClass.toString()) ?: DEFAULT)

        hideFragment(addFragment)
        hideFragment(archivedFragment)

        setupListeners()

    }

    private fun setupListeners() {

        setupBottomNavigationViewListeners()

    }

    private fun setupBottomNavigationViewListeners() {

        binding?.bottomNavigationView?.setOnItemSelectedListener { item ->

            hideFragment(supportFragmentManager.findFragmentByTag(currentFragmentTag))

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

    private fun addFragment(fragment: Fragment?, fragmentTag: String) {

        if(fragment == null)
            return

        supportFragmentManager
            .beginTransaction()
            .add(R.id.frame_layout, fragment, fragmentTag)
            .commit()

        currentFragmentTag = fragmentMap?.get(fragment.javaClass.toString()).toString()

    }

    private fun showFragment(fragment: Fragment?) {

        if(fragment == null)
            return

        supportFragmentManager
            .beginTransaction()
            .show(fragment)
            .commit()

        currentFragmentTag = fragmentMap?.get(fragment.javaClass.toString()).toString()

    }

    private fun hideFragment(fragment: Fragment?) {

        if(fragment == null)
            return

        supportFragmentManager
            .beginTransaction()
            .hide(fragment)
            .commit()

    }

}