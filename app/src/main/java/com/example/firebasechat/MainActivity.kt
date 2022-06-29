package com.example.firebasechat

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasechat.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Firebase.database
        val myRef = database.getReference("message")

        auth = Firebase.auth

        setupActionBar()

        binding.button.setOnClickListener {
            myRef.setValue(binding.inpMessage.text.toString())
        }
        onChangeListener(myRef)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navdrawer_menu,menu)

        return super.onCreateOptionsMenu(menu)
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

    private fun setupActionBar(){
        Thread{

            val bMap = Picasso.get().load(auth.currentUser!!.photoUrl).get()
            val dIcon = BitmapDrawable(resources, bMap)

            runOnUiThread {
                binding.apply {
                    bar.imageView.setImageDrawable(dIcon)
                    bar.title.text = auth.currentUser!!.displayName
                }
            }


        }.start()
    }

}