package com.example.todo.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.room.DataBase
import com.example.todo.room.TaskDao

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private var homeViewModel: HomeViewModel? = null
    private var db: DataBase? = null
    private var taskDao: TaskDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        db = DataBase.getInstance(context)
        taskDao = db?.taskDao()
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
            .taskDao(taskDao)

        homeViewModel?.isSuccess?.observe(this) { isSuccess ->

            if(!isSuccess)
                return@observe

            Log.d("ROOM_DEBUG", "taskList: ${homeViewModel?.taskList}")
            homeViewModel?.isSuccess?.value = false

        }

        homeViewModel?.getAllTasks()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding?.root
    }

}