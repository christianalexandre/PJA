package com.example.contactdefinitive


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.contactdefinitive.databinding.ActivityMainBinding


class  MainContactList : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val contactList: MutableList<Contact> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListener()
    }

    private fun setupListener() {
        with(binding) {

            buttonSaveContact.setOnClickListener {
                if (editName.text.toString() == "" || editPhone.text.toString() == "") {
                    Toast.makeText(applicationContext, getString(R.string.null_fields_toast), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (contactList.size >= 3) {
                    Toast.makeText(applicationContext, getString(R.string.full_fields_toast), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                contactList.add(Contact(editName.text.toString(), editPhone.text.toString()))
                clearEditText()
                Toast.makeText(applicationContext, getString(R.string.save_fields_toast), Toast.LENGTH_SHORT).show()
            }

            buttonClearContacts.setOnClickListener {
                contactList.clear()
                Toast.makeText(applicationContext, getString(R.string.null_saving_toast), Toast.LENGTH_SHORT).show()
            }

            buttonContactList.setOnClickListener {
                if(contactList.size > 0){
                    openSecondActivity()
                }else{
                    Toast.makeText(this@MainContactList, getString(R.string.null_list_toast), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun clearEditText() {
        with(binding) {
            editName.text.clear()
            editPhone.text.clear()
        }
    }

    private fun openSecondActivity() {
        val intent = Intent(this, ListContactActivity::class.java).apply {
            val bundle = Bundle().apply {
                putParcelableArrayList("contact", contactList.toCollection(ArrayList()))
            }
            putExtra("bundle", bundle)
        }
        startActivity(intent)
    }
}