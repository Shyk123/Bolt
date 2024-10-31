package com.example.englishproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.englishproject.databinding.ActivityMainBinding

class TwoActivity : Activity() {
    lateinit var binding: ActivityMainBinding //---------------для биндинг
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) //----------------для биндинг

        //setContentView(binding.root) //------------------------для биндинг
        setContentView(R.layout.activity_two)


        //------------------переход в другое окно
        binding.buttonOne.setOnClickListener() {
           // binding.textView123.text = "0000000000"
            val intent2: Intent = Intent(this@TwoActivity, MainActivity::class.java)
            startActivity(intent2)
        }


    }
}