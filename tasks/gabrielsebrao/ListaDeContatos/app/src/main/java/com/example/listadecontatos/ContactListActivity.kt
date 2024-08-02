package com.example.listadecontatos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import com.example.listadecontatos.databinding.ContactListActivityBinding

class ContactListActivity : ComponentActivity() {

    private lateinit var binding: ContactListActivityBinding
    private lateinit var contactList: List<Contact>

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContactListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getExtras()
        
        setupView()
        setupListeners()
    }

    private fun setupView() {
        when (contactList.size) {
            1 -> {
                with(binding) {
                    contact1.visibility = View.VISIBLE
                    nameContact1.text = contactList[0].name
                    phoneContact1.text = contactList[0].phone
                }
            }

            2 -> {
                with(binding) {
                    contact1.visibility = View.VISIBLE
                    nameContact1.text = contactList[0].name
                    phoneContact1.text = contactList[0].phone

                    contact2.visibility = View.VISIBLE
                    nameContact2.text = contactList[1].name
                    phoneContact2.text = contactList[1].phone
                }
            }

            3 -> {
                with(binding) {
                    contact1.visibility = View.VISIBLE
                    nameContact1.text = contactList[0].name
                    phoneContact1.text = contactList[0].phone

                    contact2.visibility = View.VISIBLE
                    nameContact2.text = contactList[1].name
                    phoneContact2.text = contactList[1].phone

                    contact3.visibility = View.VISIBLE
                    nameContact3.text = contactList[2].name
                    phoneContact3.text = contactList[2].phone
                }
            }
        }
    }

    private fun setupListeners() {
        binding.buttonReturn.setOnClickListener { goToMainActivity() }
    }
    private fun getExtras() {
        val bundle = intent.getBundleExtra("bundle")
        contactList = bundle?.getParcelableArrayList<Contact>("contactlist")?.toList()!!
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {
            val bundle = Bundle().apply {
                putParcelableArrayList("contactlist", contactList.toCollection(ArrayList()))
            }
            putExtra("bundle", bundle)
        }
        startActivity(intent)
    }
}
