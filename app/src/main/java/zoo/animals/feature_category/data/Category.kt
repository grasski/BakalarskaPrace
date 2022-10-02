package zoo.animals.feature_category.data

import zoo.animals.navigation.Routes

data class Category(
    val categoryName: String,
    val description: String,
    val imageId: Int,
    val route: Routes
)
