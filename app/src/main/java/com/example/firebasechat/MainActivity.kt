package com.example.firebasechat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasechat.databinding.ActivityMainBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Firebase.database
        val myRef = database.getReference("message")

        binding.button.setOnClickListener {
            myRef.setValue(binding.inpMessage.text.toString())
        }
        onChangeListener(myRef)
    }

    private fun onChangeListener(dRef:DatabaseReference){

        dRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                binding.apply {
                    rcView.append("\n")
                    rcView.append("Sergey: ${snapshot.value.toString()}")

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

}