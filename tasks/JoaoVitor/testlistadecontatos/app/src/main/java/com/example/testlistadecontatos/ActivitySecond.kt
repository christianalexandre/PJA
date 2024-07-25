package com.example.testlistadecontatos

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.testlistadecontatos.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val bundle = intent.getBundleExtra("bundle")
        val contactList = bundle?.getParcelableArrayList<Contato>("contact")


        if (contactList != null) {
            if (contactList.size == 1) {
                binding.nomeDoCtt1.text = contactList[0].name
                binding.phone1.text = contactList[0].num
                binding.barra1.visibility = View.VISIBLE
            }

            if (contactList.size == 2) {
                binding.nomeDoCtt1.text = contactList[0].name
                binding.phone1.text = contactList[0].num
                binding.nomeDoCtt2.text = contactList[1].name ?: ""
                binding.phone2.text = contactList[1].num
                binding.barra1.visibility = View.VISIBLE
                binding.barra2.visibility = View.VISIBLE
            }

            if (contactList.size == 3) {
                binding.nomeDoCtt1.text = contactList[0].name
                binding.phone1.text = contactList[0].num
                binding.nomeDoCtt2.text = contactList[1].name
                binding.phone2.text = contactList[1].num
                binding.nomeDoCtt3.text = contactList[2].name
                binding.phone3.text = contactList[2].num
                binding.barra1.visibility = View.VISIBLE
                binding.barra2.visibility = View.VISIBLE
                binding.barra3.visibility = View.VISIBLE
            }

            if (contactList.size == 0) fillDefaultValues()
        } else {
            fillDefaultValues()
        }

        fun closeActivity() {
            finish()
        }

        binding.buttonRetornar.setOnClickListener {
            closeActivity()
        }
    }

    private fun fillDefaultValues() {
        binding.nomeDoCtt1.text = ""
        binding.phone1.text = ""

        binding.nomeDoCtt2.text = ""
        binding.phone2.text = ""

        binding.nomeDoCtt3.text = ""
        binding.phone3.text = ""
    }
}
