package com.example.todolist.ui.archived

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ArchivedViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is archived Fragment"
    }
    val text: LiveData<String> = _text
}