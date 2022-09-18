package zoo.animals

import android.util.Log
import zoo.animals.data.Animal
import zoo.animals.data.AnimalData


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

fun getAnimalByName(name: String): Animal? {
    val mammals = AnimalData.allAnimalsInstance[0]
    val birds = AnimalData.allAnimalsInstance[1]
    val reptiles = AnimalData.allAnimalsInstance[2]
    val catalogAnimals = mammals + birds + reptiles

    return catalogAnimals[name]
}
