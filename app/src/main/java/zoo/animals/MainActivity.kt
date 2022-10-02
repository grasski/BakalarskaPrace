package zoo.animals


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.launch
import zoo.animals.BuildConfig.MAPS_API_KEY
import zoo.animals.animations.ContentAnimation
import zoo.animals.feature_category.data.Animal
import zoo.animals.feature_category.data.AnimalData
import zoo.animals.feature_category.data.CategoryData
import zoo.animals.feature_category.view.CategoryAnimalsScreen
import zoo.animals.feature_discovery.zoos.data.ZooData
import zoo.animals.navigation.Navigation
import zoo.animals.navigation.Routes
import zoo.animals.ui.theme.ZooTheme


class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ZooTheme{
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AnimalData.init(LocalContext.current)
                    ZooData.init(LocalContext.current)
                    ZooData.UpdateZoos()
                    PlaceApi.init(LocalContext.current)
                    Navigation()
                }
            }
        }
    }
}


object PlaceApi{
    var placesClient: PlacesClient? = null

    fun init(context: Context) {
        if (placesClient == null){
            Places.initialize(context, MAPS_API_KEY)
            placesClient = Places.createClient(context)
        }
    }
}
