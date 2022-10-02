package zoo.animals.feature_category.data


import android.content.Context

interface ICategories {
    fun mammals(context: Context): Category
    fun birds(context: Context): Category
    fun reptiles(context: Context): Category

    fun categoriesList(context: Context): List<Category>
}