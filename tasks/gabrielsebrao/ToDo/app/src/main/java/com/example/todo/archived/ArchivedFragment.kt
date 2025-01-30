package com.example.todo.archived

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.todo.databinding.FragmentArchivedBinding
import com.example.todo.room.DataBase
import com.example.todo.room.TaskDao

class ArchivedFragment : Fragment() {

    private var binding: FragmentArchivedBinding? = null
    private var archivedViewModel: ArchivedViewModel? = null
    private var db: DataBase? = null
    private var taskDao: TaskDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        db = DataBase.getInstance(context)
        taskDao = db?.taskDao()
        archivedViewModel = ViewModelProvider(this)[ArchivedViewModel::class.java]
            .taskDao(taskDao)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentArchivedBinding.inflate(layoutInflater)
        return binding?.root

    }

}