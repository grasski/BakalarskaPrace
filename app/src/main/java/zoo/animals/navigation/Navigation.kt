package zoo.animals.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import zoo.animals.feature_camera.CameraScreen
import zoo.animals.feature_category.data.Animal
import zoo.animals.feature_category.view.CategoryAnimalsScreen
import zoo.animals.feature_category.view.CategoryScreen
import zoo.animals.feature_category.view.ShowAnimalInfo
import zoo.animals.feature_discovery.zoos.data.Zoo
import zoo.animals.feature_discovery.zoos.view.DiscoveryScreen
import zoo.animals.feature_discovery.zoos.view.ZoosScreen
import zoo.animals.feature_discovery.zoos.view.ZooScreen


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

        composable(route = Routes.Discovers.route){
            DiscoveryScreen(navController)
        }
        composable(route = Routes.Zoos.route){
            ZoosScreen().ZoosScreen(navController)
        }
        composable(route = Routes.ZooInfo.route){
            val zooObject = navController.previousBackStackEntry?.savedStateHandle?.get<Zoo>("zooData")
            zooObject?.let { zoo -> ZooScreen(navController, zoo) }
        }

        composable(route = Routes.AnimalInfo.route){
            val animalObject = navController.previousBackStackEntry?.savedStateHandle?.get<Animal>("animalData")
            animalObject?.let { animal -> ShowAnimalInfo(navController = navController, animalData = animal, true) }
        }

        composable(route = Routes.Camera.route){
            CameraScreen().CameraScreen(navController)
        }
    }
}
