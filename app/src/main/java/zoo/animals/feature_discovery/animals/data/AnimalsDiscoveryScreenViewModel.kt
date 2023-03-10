package zoo.animals.feature_discovery.animals.data

import androidx.lifecycle.ViewModel
import zoo.animals.feature_category.data.Animal
import zoo.animals.feature_category.data.AnimalData

class AnimalsDiscoveryScreenViewModel: ViewModel() {
    private val _animals: MutableList<MutableMap<String, Animal>> = AnimalData.allAnimalsInstance
    val animals: MutableList<MutableMap<String, Animal>> = _animals

    fun getSeen(id: Int): Map<String, Animal> {
        return animals[id].filter { it.value.seen }
    }
    fun getNotSeen(id: Int): Map<String, Animal> {
        return animals[id].filter { !it.value.seen }
    }
    fun getAll(id: Int): Map<String, Animal> {
        return animals[id]
    }
}