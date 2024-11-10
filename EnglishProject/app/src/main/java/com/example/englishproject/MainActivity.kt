package com.example.englishproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.englishproject.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding //---------------для биндинг
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) //----------------для биндинг
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setContentView(binding.root) //------------------------для биндинг


    //  ===Старт===
        // ---Называем файлы которыми будем пользоваться в дальнейшем---
        val fileNameStorage = "notes5.txt"
        val fileNameAssets = "notes505.txt"
        // ---Проверяем наличие словаря во внутреннем хранилище, в случае отстуствия...
        if (!isFileExistsInInternalStorage(this, fileNameStorage)) {
            // ... создаём файл скопировав первичный словарь из файла в папки ассетс---
            if(createFileInInternalStorage(this, fileNameStorage, readFileFromAssets(this, fileNameAssets)))  {
                binding.textView.text = "Первый заход, словарь создан"
            }
            else{
                binding.textView.text = "Что-то пошло не так..."
            }
            binding.textView.text = "Первый заход, словарь создан"
        }

        // ---Считываем и получаем словарь---
        var contentNotesOld = readFromInternalStorage(this, fileNameStorage)
        var contentNotes = contentNotesOld

        var flagChecks = proverkaWords(contentNotes, '=') //проверяем есть ли не отвеченные слова
        binding.kolvoAll.text = flagChecks.toString()
        flagChecks = proverkaWords(contentNotes,  '+') //проверяем кол-во неверных ответов
        binding.kolvoPlysov.text = flagChecks.toString()
        flagChecks = proverkaWords(contentNotes,  '-') //проверяем кол-во верных ответов
        binding.kolvoMinysov.text = flagChecks.toString()
        var rand = 0
        var needsWords = listOf("")
        //binding.textView12.text = needsWords[0]
        binding.textViewRus.text = "."
        //binding.textView34.text = needsWords[1]

        binding.buttonLate.setOnClickListener {
            if(binding.textViewRus.text != "") {
                rand = (0..10).random()
                needsWords = findWords(contentNotes, rand)
                binding.textViewRus.text = ""
                binding.textViewEng.text = needsWords[0]
            }
            else {
                binding.textViewRus.text = needsWords[1]
            }
        }

        var textVrem = ""

        binding.buttonPlys.setOnClickListener {
            binding.textViewRus.text = needsWords[1]
            contentNotes = changeChar(contentNotes, needsWords[2].toInt(), '+')
        }
        binding.buttonMinys.setOnClickListener{
            binding.textViewRus.text = needsWords[1]
            contentNotes = changeChar(contentNotes, needsWords[2].toInt(), '-')
        }
        binding.buttonDobor.setOnClickListener{
            textVrem = ("<=" + binding.editText2.text.toString() + "/" + binding.editText1.text.toString() + ">" +'\n')
            contentNotes = contentNotes + textVrem
            writeTextToFile(this, fileNameStorage, contentNotes)
            binding.textView12.text = contentNotes
        }
        binding.buttonRestart.setOnClickListener {
            this.deleteFile(fileNameStorage)
            Toast.makeText(this, "Словарь удален", Toast.LENGTH_SHORT).show()
        }

        //------------------переход в другое окно
        binding.buttonOne.setOnClickListener() {
            val intent: Intent = Intent(this@MainActivity, TwoActivity::class.java)
            startActivity(intent)
        }

        //------------------удаление слова
        binding.buttonDelete.setOnClickListener() {
            val otvet = deleteWord(contentNotes, binding.editText2.text.toString(), binding.editText1.text.toString())
            binding.textView12.text = otvet
        }


            //-------------------------------
        //--------------------------------












    }   //========= Конец основной функции "мэин" ===

    fun isFileExistsInInternalStorage(context: Context, fileName: String): Boolean {
        val file = File(context.filesDir, fileName)
        return file.exists()
    }

    fun createFileInInternalStorage(context: Context, fileName: String, content: String): Boolean {
        return try {
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { fos ->
                fos.write(content.toByteArray())
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun readFileFromAssets(context: Context, fileName: String): String {
        val stringBuilder = StringBuilder()
        try {
            val inputStream = context.assets.open(fileName)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
                stringBuilder.append('\n')
            }
            bufferedReader.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return "Ошибка при чтении файла: ${e.message}"
        }
        return stringBuilder.toString()
    }

    fun readFromInternalStorage(context: Context, fileName: String): String {
        return try {
            context.openFileInput(fileName).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            ""
        }
    }

    fun findWords(text: String, rand: Int): List<String> {   // функция поиска слова и его перевода
        val result = mutableListOf<String>()  // результатом станет две переменные в этой колекции
        val chars = text.toCharArray()  // переводим строку в символы (получаем их в юникоде)
        var j = 0
        while ((j-1) < rand) {
            var i = 0
            while (i < chars.size) {
                if (chars[i] == '=') {
                    if(rand == j) {
                        val pozitionMurderChar = i
                        i++ // Пропускаем символ '='
                        val wordIng = StringBuilder()
                        val wordRus = StringBuilder()
                        while (i < chars.size && chars[i] != '/') {
                            wordIng.append(chars[i]) // добавляем символ в ингл слово
                            i++
                        }
                        i++ // Пропускаем символ '/'
                        while (i < chars.size && chars[i] != '>') {
                            wordRus.append(chars[i]) // добавляем символ в рус слово
                            i++
                        }
                        if (wordIng.isNotEmpty()) {  //если не пустое
                            result.add(wordIng.toString().trim()) // добавляем все символы и превращаем их в стринг **
                            result.add(wordRus.toString().trim()) // добавляем все символы и превращаем их в стринг **
                            result.add(pozitionMurderChar.toString().trim()
                            ) // добавляем кординату для найденного символа в стринг **
                            return result
                        }
                    }
                    else { j++ }
                }
                i++
            }
        }
        return result
    } // поиск слова и его перевода

    fun proverkaWords(text: String, charM: Char): Int {
        val chars = text.toCharArray()
        var counter = 0
        var i = 0
        while (i < chars.size) {
            if (chars[i] == charM) {
                counter++
            }
            i++
        }
        return counter
    } // проверяем на наличие флага указывающего статус слова

    fun writeTextToFile(context: Context, fileName: String, content: String) {
        try {
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
                outputStream.write(content.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun changeChar(text: String, oldChar: Int, newChar: Char): String {
        val chars = text.toCharArray()  // переводим строку в символы (получаем их в юникоде)
        var result = ""
        var i = 0
        while (i < chars.size) {
            if (i == oldChar) {  result += newChar  }
            else {  result += chars[i]  }
            i++
        }
        return result
    }



    fun deleteWord(text: String, engWord: String, rusWord: String): String {
        val chars = text.toCharArray()  // переводим строку в символы (получаем их в юникоде)
        val engWord1 = engWord + "                             "
        val rusWord1 = rusWord + "                             "
        val charsEng = engWord1.toCharArray()
        val charsRus = rusWord1.toCharArray()
        var result = "Nothink"
        var i = 0
        var j1 = 0
        var j2 = 0
        var n = 0
        var g = 0
        //--------------------------------
        while (i < chars.size) {
            if (chars[i] == '<') {
                n = 0; g = 0; i += 2; j1 = 0
                while (chars[i] != '/') {
                    if (charsEng[j1] == chars[i]) {
                    } else {
                        n++
                    }
                    i++; j1++
                }
                i++; j2 = 0
                while (chars[i] != '>') {
                    if (charsRus[j2] == chars[i]) {
                    } else {
                        g++
                    }
                    i++; j2++
                }
                if ((g < 2) && (n < 2)) {
                    if ((j1 == engWord.toCharArray().size) || (j1 + 1 == engWord.toCharArray().size) || (j1 - 1 == engWord.toCharArray().size)) {
                        if ((j2 == rusWord.toCharArray().size) || (j2 + 1 == rusWord.toCharArray().size) || (j2 - 1 == rusWord.toCharArray().size)) {
                            // если мы попадаем сюда то это значит что мы нашли слово под удаление ( не учтены различные вариации перевода )
                            
                            result = "Finder"
                            return result
                        }
                    }
                }
            }
            i++
        }
        return result
    }



} // последняя скобка