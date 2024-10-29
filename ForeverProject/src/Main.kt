import java.io.File
import java.io.IOException

fun main() {
    val fileName = "notes.txt"

    try {
        var textNote1 =  File(fileName).readText(Charsets.UTF_8)
        var flagChecks = proverkaWords(textNote1, fileName, '+') //проверяем есть ли не отвеченные слова
        println("Ответил на: $flagChecks" )
        flagChecks = proverkaWords(textNote1, fileName, '-') //проверяем есть ли не отвеченные слова
        println("Не ответил на: $flagChecks" )
        flagChecks = proverkaWords(textNote1, fileName, '=') //проверяем есть ли не отвеченные слова
        if  (flagChecks != 0) {
            println("Еще есть неотвеченные слова и их: $flagChecks" )
        }
        else{
            println("На все уже есть ответ, начнем с начала? Конечно да!" )
            var textNote2 = textNote1.replace('+', '=')
            textNote1 = textNote2.replace('-', '=')
            println(textNote1)
        }

        val textNote2 =  File(fileName).readText(Charsets.UTF_8)
        val rand1 = (0..10).random()
        val pointWord = findWords (textNote2, rand1)
            println(pointWord[0])
            println(pointWord[1])
            println(pointWord[2]) // pointWord[2].toInt() == chars[i])
    }
    catch (e: IOException) {
        println("pizdec where is fail ?")
    }
}

    fun proverkaWords(text: String, nameFile: String, charM: Char): Int {
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


    fun findWords(text: String, rand2: Int): List<String> {   // функция поиска слова и его перевода
        val result = mutableListOf<String>()  // результатом станет две переменные в этой колекции
        val chars = text.toCharArray()  // переводим строку в символы (получаем их в юникоде)
        var j = 0
        while ((j-1) < rand2) {
            var i = 0
            while (i < chars.size) {
                if (chars[i] == '=') {
                    if(rand2 == j) {
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