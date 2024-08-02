package com.example.contactdefinitive

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.contactdefinitive.databinding.ActivitySecondBinding

class ListContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        setupListeners()
    }

    private fun setupListeners() {
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.getBundleExtra("bundle")
        val contactList = bundle?.getParcelableArrayList<Contact>("contact")

        when (contactList != null) {
            (contactList?.size == 1) -> with(binding) {
                nomeDoCtt1.text = contactList!![0].name
                phone1.text = contactList[0].num
                barra1.visibility = View.VISIBLE
            }

            (contactList?.size == 2) -> with(binding) {
                nomeDoCtt1.text = contactList!![0].name
                phone1.text = contactList[0].num
                nomeDoCtt2.text = contactList[1].name ?: ""
                phone2.text = contactList[1].num
                barra1.visibility = View.VISIBLE
                barra2.visibility = View.VISIBLE
            }

            (contactList?.size == 3) -> with(binding) {
                nomeDoCtt1.text = contactList!![0].name
                phone1.text = contactList[0].num
                nomeDoCtt2.text = contactList[1].name
                phone2.text = contactList[1].num
                nomeDoCtt3.text = contactList[2].name
                phone3.text = contactList[2].num
                barra1.visibility = View.VISIBLE
                barra2.visibility = View.VISIBLE
                barra3.visibility = View.VISIBLE
            }

            else -> {
                Toast.makeText(this, "Nenhum contato adicionado!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        binding.buttonRetornar.setOnClickListener {
            finish()
        }
    }
}