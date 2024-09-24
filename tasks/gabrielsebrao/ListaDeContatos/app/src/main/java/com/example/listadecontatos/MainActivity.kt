package com.example.listadecontatos

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.listadecontatos.databinding.MainActivityBinding

class MainActivity : ComponentActivity() {

    private lateinit var binding: MainActivityBinding
    private var contactsList: MutableList<Contact> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getExtras()

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        setupListeners()
    }

    private fun setupListeners() {
        with(binding) {
            buttonSave.setOnClickListener { saveContact() }
            buttonClear.setOnClickListener { clearContacts() }
            buttonList.setOnClickListener { goToContactList() }
        }
    }

    private fun saveContact() {
        val name = binding.editTextName.text.toString()
        val phone = binding.editTextPhone.text.toString()

        if (name.isNotEmpty() && phone.isNotEmpty()) {
            if (contactsList.size >= 3) {
                Toast.makeText(this, R.string.exceed_three_contacts, Toast.LENGTH_SHORT).show()
            } else if (phone.length > 11) {
                Toast.makeText(this, R.string.exceed_phone_number, Toast.LENGTH_SHORT).show()
            } else if (name.length > 30) {
                Toast.makeText(this, R.string.exceed_name, Toast.LENGTH_SHORT).show()
            } else {
                try {
                    phone.toBigInteger()
                    contactsList.add(Contact(name, phone))
                    Toast.makeText(this, R.string.add_contact, Toast.LENGTH_SHORT).show()
                } catch (_: NumberFormatException) {
                    Toast.makeText(this, R.string.only_numbers, Toast.LENGTH_SHORT).show()
                } catch (_: UnsupportedOperationException) { // for some reason contactsList is changing to a singletonList
                    contactsList = mutableListOf(contactsList[0], Contact(name, phone))
                    Toast.makeText(this, R.string.add_contact, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, R.string.set_all_data, Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearContacts() {
        if (contactsList.isEmpty()) {
            Toast.makeText(this, R.string.already_empty_contacts, Toast.LENGTH_SHORT).show()
        } else {
            try {
                contactsList.clear()
            } catch(_:UnsupportedOperationException) { // for some reason contactsList is changing to a singletonList
                contactsList = mutableListOf(contactsList[0])
                contactsList.clear()
            } finally {
                Toast.makeText(this, R.string.empty_contacts, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getExtras() {
        val bundle = intent.getBundleExtra("bundle")

        if(bundle != null)
            contactsList = (bundle.getParcelableArrayList<Contact>("contactlist")?.toList() as MutableList<Contact>)
    }

    private fun goToContactList() {
        if (contactsList.isEmpty())
            Toast.makeText(this, R.string.add_at_least_one_contact, Toast.LENGTH_SHORT).show()
        else {
            val intent = Intent(this, ContactListActivity::class.java).apply {
                val bundle = Bundle().apply {
                    putParcelableArrayList("contactlist", contactsList.toCollection(ArrayList()))
                }
                putExtra("bundle", bundle)
            }
            startActivity(intent)
        }
    }
}