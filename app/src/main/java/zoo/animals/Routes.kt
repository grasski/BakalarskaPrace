package zoo.animals


sealed class Routes(val route: String) {
    object Categories : Routes("category")
    object Mammals : Routes("mammals")
    object Birds : Routes("birds")
    object Reptiles: Routes("reptiles")
    object Camera: Routes("camera")
    object AnimalInfo: Routes("animalInfo")
    object NoPermissions: Routes("noPermissions")
}