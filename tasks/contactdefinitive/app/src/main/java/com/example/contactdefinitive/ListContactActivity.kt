package com.example.contactdefinitive


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.contactdefinitive.databinding.ActivitySecondBinding

class ListContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    private var contactList: List<Contact>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getExtras()
        setupListeners()
        setupView()
    }
    private fun getExtras(){
        val bundle = intent.getBundleExtra("bundle")
        contactList = bundle?.getParcelableArrayList<Contact>("contact")?.toList()
    }

    private fun setupListeners() {
        binding.buttonReturn.setOnClickListener {
            finish()
        }
    }

    private fun setupView() {
        with(binding) {
            when (contactList!!.size) {
                1 -> {
                    bar1.visibility = View.VISIBLE
                    nameContact1.text = contactList!![0].name
                    phone1.text = contactList!![0].num
                }

                2 -> {
                    bar1.visibility = View.VISIBLE
                    bar2.visibility = View.VISIBLE
                    nameContact1.text = contactList!![0].name
                    phone1.text = contactList!![0].num
                    nameContact2.text = contactList!![1].name
                    phone2.text = contactList!![1].num
                }

                3 -> {
                    bar1.visibility = View.VISIBLE
                    bar2.visibility = View.VISIBLE
                    bar3.visibility = View.VISIBLE
                    nameContact1.text = contactList!![0].name
                    phone1.text = contactList!![0].num
                    nameContact2.text = contactList!![1].name
                    phone2.text = contactList!![1].num
                    nameContact3.text = contactList!![2].name
                    phone3.text = contactList!![2].num
                }

                else -> {
                    print("error")
                }
            }
        }
    }
}