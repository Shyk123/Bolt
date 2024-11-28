package com.example.justtest

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.justtest.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

import android.widget.Toast //------------------------- для вызова "toast"

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
        val fileNameAssets = "notes505.txt"
        val fileNameStorage = "notes5.txt"
        var contentNotes = readFromInternalStorage(this, fileNameStorage)
        var contentNotesNew = ""

        // ---Проверяем наличие словаря во внутреннем хранилище, в случае отстуствия---
        binding.buttonCheck.setOnClickListener {
            if (!isFileExistsInInternalStorage(this, fileNameStorage)) {
                // ... создаём файл скопировав первичный словарь из файла в папки ассетс---
                if (createFileInInternalStorage(
                        this,
                        fileNameStorage,
                        readFileFromAssets(this, fileNameAssets)
                    )
                ) {
                    Toast.makeText(this, "Первый заход, словарь создан", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Что-то пошло не так...", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Вижу словарь", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonDel.setOnClickListener {
            if (isFileExistsInInternalStorage(this, fileNameStorage)) {
                this.deleteFile(fileNameStorage)
                Toast.makeText(this, "Словарь удален", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Словаря и так нет", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonOutput.setOnClickListener {
            if (isFileExistsInInternalStorage(this, fileNameStorage)) {
                contentNotes = readFromInternalStorage(this, fileNameStorage)
                binding.textWorld.text = contentNotes
            } else {
                Toast.makeText(this, "Нечего выводить(", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonDelWord.setOnClickListener {

            if (binding.editTextEng.text.toString() != "") {
                if (binding.editTextRus.text.toString() != "") {

                    contentNotesNew = deletWords(
                        contentNotes,
                        binding.editTextEng.text.toString(),
                        binding.editTextRus.text.toString()
                    )
                    if (contentNotes != contentNotesNew) {
                        binding.textWorld.text = contentNotesNew
                        contentNotes = contentNotesNew
                        contentNotesNew = ""
                        createFileInInternalStorage(this, fileNameStorage, contentNotes)
                    } else {
                        binding.textWorld.text = "нету такого сочитания"
                    }
                } else {
                    binding.textWorld.text = "не ввел русское слово"
                }
            } else {
                binding.textWorld.text = "не ввел англиское слово"
            }
        }

    }//========================    Последняя скоба для метода onCreat  ==========================

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

    fun deletWords(text: String, engWord: String, rusWord: String): String {
        // получаем элементы массива из пришедшей строки
        var textNew = text.split("\n")

        var slovaVstroke  = mutableListOf<String>()
        var i = 0
        var j = 0
        var j2 = 0
        var n = 1
        var g = 0
        var g2 = 0
        var poz = 0
        var j3 = 0
        var textNew2= mutableListOf<Char>()
        var textPriom = mutableListOf<Char>()
        var textTotal: String = ""

        var charsEng = engWord.toCharArray()
        var charsRus = rusWord.toCharArray()

        // гуляем по циклу пока есть элементы в массиве или мы не найдем искомое
        while (i < textNew.count()-1) {        // последний элемент всегда пустой

            j = 2 // пропускаем '<' and '+/-/='
            n = 1 // кол-во слов в "массиве текущего элемента"
            textNew2 = textNew[i].toMutableList() // строка для работы

            while (textNew2[j] != '/') {      // take eng. word
                textPriom.add(textNew2[j])  // посимвольно собираем слово
                j++
            }
            j++ // пропускаем слэшь

            slovaVstroke.add(textPriom.joinToString("")) // английское слово записали
            textPriom = mutableListOf<Char>()// обнуляем

            while (textNew2[j] != '>') {                // добываем перевод
                if (textNew2[j] != ' ') {               // на всякий случай игнорим пробелы
                    if (textNew2[j] != ',') {           // может быть несколько переводов одного слова
                        textPriom.add(textNew2[j])      // посимвольно собираем перевод слова
                        j++
                    } else {
                        j++
                        slovaVstroke.add(textPriom.joinToString(""))
                        textPriom = mutableListOf<Char>() // обнуляем
                        n++
                    }
                }
                else { j++ }
            }
            slovaVstroke.add(textPriom.joinToString("")) // получаем перевод

// --------------------- основная проверка ------- самих слов друг с другом ------------------------

            poz = 0
            textPriom = mutableListOf<Char>()
            textPriom.addAll(slovaVstroke[0].toList())
            j2 = textPriom.size
            j = 0
            g = 0

            if ((j2 == engWord.toCharArray().size) || (j2 + 1 == engWord.toCharArray().size) || (j2 - 1 == engWord.toCharArray().size)) {

                if (j2 != engWord.toCharArray().size) {        // проверяем размер, если нет то на позицию

                    if ((charsEng[0] == textPriom[1]) && (charsEng[1] == textPriom[2])) {
                        poz = 1
                        g = 1
                        j = 1
                    }
                    if ((charsEng[1] == textPriom[0]) && (charsEng[2] == textPriom[1])) {
                        poz = -1
                        g = 1
                        j = 1
                    }
                }
            } else {
                g = 3
            }

            while ((j < (j2-1)) && (g < 2)) {
                if (charsEng[j] == textPriom[j + poz]) {
                } else {
                    if (charsEng[j] == textPriom[j + 1]) {
                        poz = 1
                    }
                    g++
                }
                j++
            }   // типо проверили
            poz = 0

            if (g < 2) {    // ----- попадаем сюда если в слове менее двух ошибок
                j3 = 1
                while (j3 < n + 1) {
                    textPriom = mutableListOf<Char>()    // обнуляем
                    //  textPriom = slovaVstroke[j].toMutableList() // вылет
                    textPriom.addAll(slovaVstroke[j3].toList())
                    j2 = textPriom.size
                    g2 = 0
                    j = 0
                    //--------------и тут типо основная проверка «два»------------------------=======================================
                    if ((j2 == rusWord.toCharArray().size) || (j2 + 1 == rusWord.toCharArray().size) || (j2 - 1 == rusWord.toCharArray().size)) {

                        if (j2 != rusWord.toCharArray().size) {        // проверяем размер, если нет то на позицию

                            if ((charsRus[0] == textPriom[1]) && (charsRus[1] == textPriom[2])) {
                                poz = 1
                                g2 = 1
                            }
                            if ((charsRus[1] == textPriom[0]) && (charsRus[2] == textPriom[1])) {
                                poz = -1
                                g2 = 1
                                j = 1
                            }
                        }
                    } else {
                        g2 = 3
                    }

                    while ((j < (j2-1)) && (g2 < 2)) {                  // для Рус нужно еще обработать: 'n' слов
                        if (charsRus[j] == textPriom[j + poz]) {
                        } else {
                            if (charsRus[j] == textPriom[j + 1]) {
                                poz = 1
                            }
                            g++
                        }
                        j++
                    }

                    if ((g2 < 2 ) && (g < 2 )  ) {
                        j2 = 0
                        while (j2 < textNew.count()) {
                            if (textNew[j2].toCharArray().size > 1) {
                                if (j2 != i) {
                                    textTotal += textNew[j2]
                                    if (j2 + 1 != textNew.count()) {
                                        textTotal += '\n'
                                    }
                                }
                            }
                            j2++
                        }
                        if ((g == 1) || (g2 == 1)) {
                            Toast.makeText(this, "Ц, почти: " +slovaVstroke[0] +" " +slovaVstroke[n], Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Toast.makeText(this, "Юху - все четко", Toast.LENGTH_SHORT).show()
                        }
                        return textTotal
                    }

                }   // типо проверили
                //============================================================================================
            }
            slovaVstroke = mutableListOf<String>()
            textPriom = mutableListOf<Char>()
            i++
        }
        return text
        }

    } // ======================= скоба края ===============================