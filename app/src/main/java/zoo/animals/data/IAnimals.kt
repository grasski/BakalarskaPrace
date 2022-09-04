package zoo.animals.data

import android.content.Context

interface IAnimals {
    fun mammals(context: Context): MutableMap<String, Animal>
    fun birds(context: Context): MutableMap<String, Animal>
    fun reptiles(context: Context): MutableMap<String, Animal>

    fun animalsToList(
        context: Context,
        category: String,
        animals: List<Int>,
        images: List<List<Int>>
    ): MutableMap<String, Animal>
}