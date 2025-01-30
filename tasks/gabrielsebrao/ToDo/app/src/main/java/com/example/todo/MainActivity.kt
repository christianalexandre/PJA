package com.example.todo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.todo.add.AddFragment
import com.example.todo.archived.ArchivedFragment
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.home.HomeFragment
import com.example.todo.room.DataBase

private const val HOME_FRAGMENT = "home_fragment"
private const val ARCHIVED_FRAGMENT = "archived_fragment"
private var ADD_FRAGMENT = "add_fragment"

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var db: DataBase? = null
    private var homeFragment: Fragment? = null
    private var archivedFragment: Fragment? = null
    private var addFragment: Fragment? = null
    private var fragmentMap: Map<String, String>? = null
    private var currentFragmentTag: String = HOME_FRAGMENT

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        db = DataBase.getInstance(this)

        setupFragments()
        setupListeners()

    }

    override fun onResume() {

        super.onResume()

        val activeFragment = supportFragmentManager.fragments.find { !it.isHidden }
        currentFragmentTag = activeFragment?.tag ?: HOME_FRAGMENT

    }

    private fun setupFragments() {

        homeFragment = supportFragmentManager.findFragmentByTag(HOME_FRAGMENT) ?: HomeFragment()
        archivedFragment = supportFragmentManager.findFragmentByTag(ARCHIVED_FRAGMENT) ?: ArchivedFragment()
        addFragment = supportFragmentManager.findFragmentByTag(ADD_FRAGMENT) ?: AddFragment()

        if(supportFragmentManager.findFragmentByTag(ADD_FRAGMENT) == null) {
            addFragment(addFragment, ADD_FRAGMENT)
            hideFragment(addFragment)
        }

        if(supportFragmentManager.findFragmentByTag(ARCHIVED_FRAGMENT) == null) {
            addFragment(archivedFragment, ARCHIVED_FRAGMENT)
            hideFragment(archivedFragment)
        }

        if(supportFragmentManager.findFragmentByTag(HOME_FRAGMENT) == null)
            addFragment(homeFragment, HOME_FRAGMENT)

        fragmentMap = mapOf(
            Pair(addFragment?.javaClass.toString(), ADD_FRAGMENT),
            Pair(archivedFragment?.javaClass.toString(), ARCHIVED_FRAGMENT),
            Pair(homeFragment?.javaClass.toString(), HOME_FRAGMENT)
        )

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
            currentFragmentTag = fragmentMap?.get(fragment?.javaClass.toString()) ?: currentFragmentTag

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

    }

    private fun showFragment(fragment: Fragment?) {

        if(fragment == null)
            return

        supportFragmentManager
            .beginTransaction()
            .show(fragment)
            .commit()

        if(fragment is HomeFragment)
            fragment.getAllTasks()

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