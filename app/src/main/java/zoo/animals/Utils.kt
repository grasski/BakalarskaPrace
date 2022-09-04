package zoo.animals


fun stringMapToIndexKey(map: Map<String, Any>): Map<Int, String> {
    val keys: Set<String> = (listOf(map.keys)[0])

    return keys.mapIndexed { index: Int, value: String -> index to value }.toMap()
}

fun String.normalize(): String {
    val original = arrayOf("ě", "é", "š", "č", "ř", "ž", "ý", "á", "í", "ú", "ů", "ó", "ň")
    val normalized = arrayOf("e", "e", "s", "c", "r", "z", "y", "a", "i", "u", "u", "o", "n")

    return this.map {
        val index = original.indexOf(it.toString())
        if (index >= 0) normalized[index] else it
    }.joinToString("")
}
