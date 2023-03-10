package zoo.animals.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import zoo.animals.feature_about.About
import zoo.animals.feature_camera.view.CameraScreen
import zoo.animals.feature_category.data.Animal
import zoo.animals.feature_category.view.CategoryAnimalsScreen
import zoo.animals.feature_category.view.CategoryScreen
import zoo.animals.feature_category.view.ShowAnimalInfo
import zoo.animals.feature_discovery.zoos.data.Zoo
import zoo.animals.feature_discovery.animals.AnimalsDiscoveryScreen
import zoo.animals.feature_discovery.DiscoveryScreen
import zoo.animals.feature_discovery.zoos.ZoosDiscoveryScreen
import zoo.animals.feature_discovery.zoos.view.view.ZooScreen
import zoo.animals.feature_settings.Settigns
import zoo.animals.feature_welcome.view.WelcomeScreen


@Composable
fun Navigation(
    startDestination: String = Routes.Welcome.route
){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination){
        composable(route = Routes.Welcome.route){
            WelcomeScreen(navController = navController)
        }

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
            ZoosDiscoveryScreen().ZoosDiscoveryScreen(navController)
        }
        composable(route = Routes.ZooInfo.route){
            val zooObject = navController.previousBackStackEntry?.savedStateHandle?.get<Zoo>("zooData")
            zooObject?.let { zoo -> ZooScreen(navController, zoo) }
        }
        composable(route = Routes.MammalsDiscovery.route){
            AnimalsDiscoveryScreen().MammalsDiscoveryScreen(navController)
        }
        composable(route = Routes.BirdsDiscovery.route){
            AnimalsDiscoveryScreen().BirdsDiscoveryScreen(navController)
        }
        composable(route = Routes.ReptilesDiscovery.route){
            AnimalsDiscoveryScreen().ReptilesDiscoveryScreen(navController)
        }

        composable(route = Routes.AnimalInfo.route){
            val animalObject = navController.previousBackStackEntry?.savedStateHandle?.get<Animal>("animalData")
            animalObject?.let { animal ->
                ShowAnimalInfo(navController = navController, animalData = animal, true)
            }
        }

        composable(route = Routes.Camera.route){
            CameraScreen().CameraScreen(navController)
        }

        composable(route = Routes.Settings.route){
            Settigns(navController)
        }
        composable(route = Routes.About.route){
            About(navController)
        }
    }
}
