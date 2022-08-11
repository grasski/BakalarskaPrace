package zoo.animals.data

import zoo.animals.Routes

data class Category(
    val categoryName: String,
    val description: String,
    val imageId: Int,
    val route: Routes
)
