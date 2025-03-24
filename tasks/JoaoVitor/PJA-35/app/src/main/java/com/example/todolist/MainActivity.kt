package com.example.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.databinding.ItemBottomNavBinding
import com.example.todolist.ui.adapter.PageAdapter
import com.example.todolist.ui.add.AddFragment
import com.example.todolist.ui.archived.ArchivedFragment
import com.example.todolist.ui.home.HomeFragment
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupFragments()
        setupPageChangeListener()
    }

    private fun setupFragments() {
        val fragmentList = listOf(
            Triple(R.drawable.ic_home_black_24dp, "Home", HomeFragment()),
            Triple(R.drawable.ic_archived_24dp, "Arquivados", ArchivedFragment()),
            Triple(R.drawable.ic_add_24dp, "Adicionar", AddFragment())
        )

        val pageAdapter = PageAdapter(this)
        pageAdapter.fragments.addAll(fragmentList.map { it.third })

        with(binding.vp) {
            offscreenPageLimit = 3
            adapter = pageAdapter
        }

        TabLayoutMediator(binding.bottomNavigation, binding.vp) { tab, position ->
            tab.customView = createTabView(fragmentList[position].first, fragmentList[position].second)
        }.attach()
    }

    private fun createTabView(@DrawableRes iconRes: Int, text: String): View {
        val tabBinding = ItemBottomNavBinding.inflate(LayoutInflater.from(this))
        tabBinding.icon.setImageResource(iconRes)
        tabBinding.label.text = text
        return tabBinding.root
    }

    private fun setupPageChangeListener() {
        binding.vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val pageAdapter = binding.vp.adapter as? PageAdapter
                val fragment = pageAdapter?.fragments?.getOrNull(position)

                if (fragment is ArchivedFragment) {
                    fragment.checkScreen()
                } else if (fragment is HomeFragment) {
                    fragment.checkScreen()
                }
            }
        })
    }
}