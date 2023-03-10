package zoo.animals.feature_discovery.zoos.data

import androidx.lifecycle.ViewModel

class ZoosDiscoveryScreenViewModel: ViewModel() {
    private val _zoos: MutableMap<String, Zoo> = ZooData.allZoosInstance
    val zoos: MutableMap<String, Zoo> = _zoos.toSortedMap()

    fun getVisited(): Map<String, Zoo> {
        return zoos.filter { it.value.visited }
    }
    fun getNotVisited(): Map<String, Zoo>{
        return zoos.filter { !it.value.visited }
    }
    fun getAll(): MutableMap<String, Zoo> {
        return zoos
    }
}