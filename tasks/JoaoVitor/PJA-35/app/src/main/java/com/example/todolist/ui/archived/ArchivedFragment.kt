package com.example.todolist.ui.archived

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.databinding.FragmentArchivedBinding

class ArchivedFragment : Fragment() {

    private var _binding: FragmentArchivedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val archivedViewModel =
            ViewModelProvider(this).get(ArchivedViewModel::class.java)

        _binding = FragmentArchivedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textArchived
        archivedViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}