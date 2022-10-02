package zoo.animals.feature_discovery.zoos.view


import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import zoo.animals.PlaceApi.placesClient
import zoo.animals.feature_discovery.zoos.data.InfoState
import zoo.animals.feature_discovery.zoos.data.Zoo


class InfoScreenModel: ViewModel() {
    val state by mutableStateOf(InfoState())

    private val DEFAULT_PLACE_FIELDS = listOf(
        Place.Field.RATING,
        Place.Field.OPENING_HOURS,
        Place.Field.UTC_OFFSET
    )

    fun fetchPlace(zoo: Zoo){
        if (state.place.value == null){
            val placeId = zoo.placeID
            val placeFields: List<Place.Field> = DEFAULT_PLACE_FIELDS
            state.loading.value = true
            val request = FetchPlaceRequest.newInstance(placeId, placeFields)
            placesClient!!.fetchPlace(request).addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response.place
                state.place.value = place
                state.loading.value = false
                state.error.value = null
                Log.d(this.javaClass.name, "RESPONSE")
            }.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    state.loading.value = false
                    state.error.value = exception
                }
                Log.d(this.javaClass.name, "ERROR")
            }
        } else{
            Log.d(this.javaClass.name, "RESPONSE, LOADING FROM MEMORY")
        }
    }
}




