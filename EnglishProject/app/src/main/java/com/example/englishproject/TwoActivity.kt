package com.example.englishproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.englishproject.databinding.ActivityTwoBinding


class TwoActivity : Activity() {
    lateinit var binding: ActivityTwoBinding //---------------для биндинг
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTwoBinding.inflate(layoutInflater) //----------------для биндинг

        //setContentView(binding.root) //------------------------для биндинг
        //setContentView(R.layout.activity_two)
        setContentView(binding.root) //------------------------для биндинг


        val fileNameStorage = "notes5.txt"
        var contentNotesOld = readFromInternalStorage(this, fileNameStorage)
        var contentNotes = contentNotesOld









        //------------------проверяем наличие того что н-н-надо
        binding.buttonCheck.setOnClickListener {
            val engWord = binding.editTextOne.toString()
            val rusWord = binding.editTextTwo.toString()
            var n = 0

            if (n==1) {
                Toast.makeText(this, "Найденно и верно", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Нифига такого нет", Toast.LENGTH_SHORT).show()
            }
        }


        //------------------переход в другое окно
        binding.buttonBack.setOnClickListener() {
           // binding.textView123.text = "0000000000"
            val intent2: Intent = Intent(this@TwoActivity, MainActivity::class.java)
            startActivity(intent2)
        }

    }
    //-------------------------
    // --------------------Функционал--------------------------
    fun readFromInternalStorage(context: Context, fileName: String): String {
        return try {
            context.openFileInput(fileName).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            ""
        }
    }



}