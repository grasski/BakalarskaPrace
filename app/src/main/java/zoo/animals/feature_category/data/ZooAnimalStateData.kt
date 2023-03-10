package zoo.animals.feature_category.data

import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import zoo.animals.feature_discovery.zoos.data.Zoo
import zoo.animals.feature_discovery.zoos.data.ZooData

data class ZooAnimalStateData(
    var zoos: MutableMap<String, Zoo> = ZooData.allZoosInstance.toSortedMap(),
    var selectedZoo: MutableState<Zoo?> = mutableStateOf(null),
    var selectedZooKey: MutableState<String?> = mutableStateOf(null),

    var image1: MutableState<Int?> = mutableStateOf(null),
    var image2: MutableState<Int?> = mutableStateOf(null),
    var image3: MutableState<Int?> = mutableStateOf(null),

    var isGpsEnabled: MutableState<Boolean> = mutableStateOf(false),
    var currentUserLocation: MutableState<Location?> = mutableStateOf(null),
    var closestZooKey: MutableState<String?> = mutableStateOf(null)
)
