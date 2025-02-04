package com.example.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.ui.adapter.PageAdapter
import com.example.todolist.ui.add.AddFragment
import com.example.todolist.ui.archived.ArchivedFragment
import com.example.todolist.ui.home.HomeFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupFragments()
    }

    private fun setupFragments() {
        val fragmentList = listOf(
            Triple(R.drawable.ic_home_black_24dp, "Home", HomeFragment()),
            Triple(R.drawable.ic_archived_24dp, "Arquivados", ArchivedFragment()),
            Triple(R.drawable.ic_add_24dp, "Adicionar", AddFragment())
        )

        val pageAdapter = PageAdapter(this)
        pageAdapter.fragments.addAll(fragmentList.map { it.third })

        with(binding) {
            with(vp) {
                offscreenPageLimit = 3
                adapter = pageAdapter
            }
            TabLayoutMediator(bottomNavigation, vp) { tab, position ->
                tab.customView = createTabView(fragmentList[position].first, fragmentList[position].second)
            }.attach()
        }
    }

    private fun createTabView(@DrawableRes iconRes: Int, text: String): View {
        val view = LayoutInflater.from(this).inflate(R.layout.item_bottom_nav, null)
        val icon = view.findViewById<ImageView>(R.id.icon)
        val label = view.findViewById<TextView>(R.id.label)

        icon.setImageResource(iconRes)
        label.text = text

        return view
    }
}
