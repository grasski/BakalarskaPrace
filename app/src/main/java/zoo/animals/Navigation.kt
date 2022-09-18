package zoo.animals


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import zoo.animals.data.Animal
import zoo.animals.screens.*


@Composable
fun Navigation(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Categories.route){
        composable(route = Routes.Categories.route){
            CategoryScreen(navController)
        }
        composable(route = Routes.Mammals.route){
            CategoryAnimalsScreen().MammalsScreen(navController)
        }
        composable(route = Routes.Birds.route){
            CategoryAnimalsScreen().BirdsScreen(navController)
        }
        composable(route = Routes.Reptiles.route){
            CategoryAnimalsScreen().ReptilesScreen(navController)
        }

        composable(route = Routes.AnimalInfo.route){
            val animalObject = navController.previousBackStackEntry?.savedStateHandle?.get<Animal>("animalData")
            animalObject?.let { it1 -> ShowAnimalInfo(navController = navController, animalData = it1, true) }
        }

        composable(route = Routes.Camera.route){
            CameraScreen().CameraScreen(navController)
        }
    }
}
