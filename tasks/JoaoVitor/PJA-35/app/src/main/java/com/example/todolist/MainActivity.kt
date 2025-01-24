package com.example.todolist

import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.ui.adapter.PageAdapter
import com.example.todolist.ui.add.AddFragment
import com.example.todolist.ui.archived.ArchivedFragment
import com.example.todolist.ui.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupFragments()
//        val navView: BottomNavigationView = binding.navView
//
//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_archived, R.id.navigation_add
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
    }

    private fun setupFragments() {
        val fragmentList = listOf(
            Pair(createImageView(R.drawable.ic_home_black_24dp), HomeFragment()),
            Pair(createImageView(R.drawable.ic_archived_24dp), ArchivedFragment()),
            Pair(createImageView(R.drawable.ic_add_24dp), AddFragment()),
        )
        val pageAdapter = PageAdapter(this)

        pageAdapter.fragments.addAll(fragmentList.map { it.second })

        with(binding) {
            with(vp) {
                offscreenPageLimit = 3
                currentItem = 0
                adapter = pageAdapter
            }
            TabLayoutMediator(bottomNavigation, vp) { tab, position ->
                tab.customView = fragmentList[position].first
            }.attach()
        }
    }

    private fun createImageView(@DrawableRes drawableRes: Int): ImageView {
        return ImageView(this).apply {
            setImageResource(drawableRes)
            setPadding(30,0,30,0)
            imageTintList = AppCompatResources.getColorStateList(this@MainActivity, R.color.color_menu)
        }
    }
}