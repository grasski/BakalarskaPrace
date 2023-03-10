package zoo.animals.feature_category.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import zoo.animals.MainActivity
import zoo.animals.UiTexts
import zoo.animals.feature_discovery.zoos.data.ZooData
import zoo.animals.stringMapToIndexKey


class ZooAnimalViewModel: ViewModel() {
    val state by mutableStateOf(ZooAnimalStateData())

    private val _informations = MutableStateFlow(listOf<String?>(null))
    val informations = _informations.asStateFlow()

    private val _validZooKeys = MutableStateFlow(listOf<String?>(null))
    val validZooKeys = _validZooKeys.asStateFlow()

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            for (location in p0.locations) {
                state.currentUserLocation.value = location

                val tempClosestZooKey = state.closestZooKey.value
                state.closestZooKey.value = getClosestZooKey()

                if (tempClosestZooKey != state.closestZooKey.value){
                    state.selectedZoo.value = state.zoos[state.closestZooKey.value]
                    state.selectedZooKey.value = state.closestZooKey.value
                }
            }
        }
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCleared() {
        super.onCleared()

        if (this::fusedLocationClient.isInitialized){
            locationCallback.let { fusedLocationClient.removeLocationUpdates(it) }
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (this::fusedLocationClient.isInitialized){
            locationCallback.let { fusedLocationClient.removeLocationUpdates(it) }
        }

        locationCallback.let {
            val locationRequest = LocationRequest.Builder(600_000).setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY).build()
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }

    init {
        state.isGpsEnabled.value = false
        state.currentUserLocation.value = null
        state.closestZooKey.value = null

        state.zoos = ZooData.allZoosInstance.toSortedMap()
        val keys = stringMapToIndexKey(state.zoos)

        state.selectedZoo.value = state.zoos[keys[0]]
        state.selectedZooKey.value = keys[0]
    }

    @SuppressLint("DiscouragedApi")
    fun getAnimalsInZoo(context: Context, animalKey: String) {
        val zoos = context.resources.getIdentifier(animalKey+"Zoo", "array", context.packageName)
        _validZooKeys.value = if (zoos == 0) listOf(null) else UiTexts.ArrayResource(zoos, 0).asArray(context).sorted()
        if (state.selectedZooKey.value !in _validZooKeys.value && _validZooKeys.value.isNotEmpty()){
            state.selectedZooKey.value = _validZooKeys.value[0]

            state.selectedZoo.value = state.zoos[state.selectedZooKey.value]
        }

        val res = context.resources.getIdentifier(state.selectedZooKey.value + animalKey, "array", context.packageName)
        if (res == 0) {
            _informations.value = listOf(null)

            state.image1.value = null
            state.image2.value = null
            state.image3.value = null
            return
        }

        var imgRes = context.resources.getIdentifier((state.selectedZooKey.value+"_"+animalKey+"_1").lowercase(), "drawable", context.packageName)
        state.image1.value = if(imgRes != 0){imgRes} else null

        imgRes = context.resources.getIdentifier((state.selectedZooKey.value+"_"+animalKey+"_2").lowercase(), "drawable", context.packageName)
        state.image2.value = if(imgRes != 0){imgRes} else null

        imgRes = context.resources.getIdentifier((state.selectedZooKey.value+"_"+animalKey+"_3").lowercase(), "drawable", context.packageName)
        state.image3.value = if(imgRes != 0){imgRes} else null


        _informations.value = UiTexts.ArrayResource(res, 0).asArray(context)
    }

    fun getClosestZooKey(): String? {
        var closestZooKey: String? = null
        var smallestDistance = Float.MAX_VALUE

        for (entry in state.zoos) {
            val zooLoc = Location("")
            zooLoc.latitude = entry.value.position[0]
            zooLoc.longitude = entry.value.position[1]

            val distance = state.currentUserLocation.value?.distanceTo(zooLoc)
            if (distance != null) {
                if (distance < smallestDistance) {
                    smallestDistance = distance
                    closestZooKey = entry.key
                }
            }
        }

        return closestZooKey
    }
}
