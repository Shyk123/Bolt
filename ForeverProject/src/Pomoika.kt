//    val textZapis = "World-42"
//     File(fileName).writeText(textZapis)

/*
        val text =  File(fileName).readText(Charsets.UTF_8)
        val charsToCheck = listOf('+', '=')

        for (i in 0 until text.length) {
            val currentChar = text[i]
            when (currentChar) {
                '+' -> {
                    println("+")

                }
                '-' -> println("-")
                '=' -> println("=")
            }
        }
         /*   if (currentChar in charsToCheck) {
                val nextChar = text[i + 1]
                println("После '$currentChar' следует символ: $nextChar")
            }
            */





fun extractWords(text: String): List<String> {
    val result = mutableListOf<String>()
    val chars = text.toCharArray()
    var i = 0

    while (i < chars.size) {
        if (chars[i] == '+' || chars[i] == '=') {
            i++ // Пропускаем символ '+' или '='
            val word = StringBuilder()
            while (i < chars.size && chars[i] != '/') {
                word.append(chars[i])
                i++
            }
            if (word.isNotEmpty()) {
                result.add(word.toString().trim())
            }
        }
        i++
    }

    return result
}

fun main() {
    val text = "Это+пример/текста=с разными/символами"
    val extractedWords = extractWords(text)
    println("Найденные слова: $extractedWords")
}





        val totalCharacters = text.length

        data class Employee(val name:String, val age:Int)
    data class Department(val name: String, val employees: List<Employee>)
data class Company(val name: String, val departments: List<Department>)

val emp1 = Employee( "Kirill", 23 )
val emp2 = Employee( "Kyrva", 63 )
val emp3 = Employee( "Rvach", 48 )
val emp4 = Employee( "Achuy", 19 )

val dep1 = Department( "Hylio" , listOf(emp1, emp2, emp3))
val dep2 = Department( "HylioNado" , listOf(emp1, emp4))
val dep3 = Department( "HylioJdesh" , listOf(emp2, emp3, emp4, emp1))

val com = Company("GoG" , listOf(dep1, dep2, dep3))

val nonstop1 = 0
val nonstop2 = 0
var colvo4elovek = 0

for (nonstop1 in com.departments) {
    println(" debil " + nonstop1.employees.size)
    for (nonstop2 in nonstop1.employees) {
    colvo4elovek++

    }

}
println("  $colvo4elovek    ")

val s = 56
when(s)
{
    1 -> println("1")
    2 -> println("2")
    3 -> println("3")
    else -> println("mnogo")
}
println("  $s    ")

 */