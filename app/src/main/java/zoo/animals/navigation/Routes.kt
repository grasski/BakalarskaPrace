package zoo.animals.navigation


sealed class Routes(val route: String) {
    object Categories : Routes("category")

    object Mammals : Routes("mammals")
    object Birds : Routes("birds")
    object Reptiles: Routes("reptiles")
    object AnimalInfo: Routes("animalInfo")

    object Camera: Routes("camera")

    object About: Routes("about")
    object Settings: Routes("settings")

    object Discovers: Routes("discover")
    object Zoos: Routes("zoo")
    object ZooInfo: Routes("zooInfo")
    object AnimalsDiscovery: Routes("animalsDiscovery")
}