package com.example.firebasechat

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
    lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Firebase.database
        val myRef = database.getReference("message")

        auth = Firebase.auth

        setupActionBar()

        binding.button.setOnClickListener {
            myRef.child(myRef.push().key ?: "lolka").setValue(User(auth.currentUser?.displayName,binding.inpMessage.text.toString()))
        }
        onChangeListener(myRef)
        initRcView()
    }

    private fun initRcView(){
        adapter = UserAdapter()
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navdrawer_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    private fun onChangeListener(dRef:DatabaseReference){

        dRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<User>()
                for(s in snapshot.children){
                    val user = s.getValue(User::class.java)
                    if(user != null)list.add(user)
                }
                adapter.submitList(list)

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