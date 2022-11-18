package zoo.animals


import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.gson.annotations.SerializedName
import io.ktor.client.*
import io.ktor.serialization.gson.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import zoo.animals.BuildConfig.MAPS_API_KEY
import zoo.animals.feature_category.data.AnimalData
import zoo.animals.feature_discovery.zoos.data.ZooData
import zoo.animals.navigation.Navigation
import zoo.animals.ui.theme.ZooTheme


class MainActivity : ComponentActivity() {
    @OptIn(InternalAPI::class)
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
                    val scope = rememberCoroutineScope()
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
