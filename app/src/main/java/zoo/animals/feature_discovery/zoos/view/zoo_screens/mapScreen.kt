package zoo.animals.feature_discovery.zoos.view.zoo_screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import zoo.animals.feature_discovery.zoos.data.Zoo

@Composable
fun MapScreen(zoo: Zoo) {
    val city = LatLng(zoo.position[0], zoo.position[1])
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(city, 15f)
    }
    val uiSettings = remember{
        MapUiSettings(
            compassEnabled = true,
            myLocationButtonEnabled = false,
            mapToolbarEnabled = true,
            indoorLevelPickerEnabled = false
        )
    }
    Box(modifier = Modifier.fillMaxSize()){
        GoogleMap(
            modifier = Modifier
                .matchParentSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = uiSettings,
            properties = MapProperties(mapType = MapType.TERRAIN)
        ){
            Marker(
                state = MarkerState(position = city),
                title = zoo.city,
                snippet = zoo.type + "\n" + zoo.city
            )
        }
    }
}
