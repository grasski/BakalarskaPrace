package zoo.animals


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import io.ktor.util.*
import zoo.animals.BuildConfig.MAPS_API_KEY
import zoo.animals.feature_welcome.data.WelcomeScreenViewModel
import zoo.animals.navigation.Navigation
import zoo.animals.navigation.Routes
import zoo.animals.ui.theme.ZooTheme


class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = this.baseContext
        val welcomeScreenViewModel = WelcomeScreenViewModel(context)

        installSplashScreen().setKeepOnScreenCondition{
            welcomeScreenViewModel.appIsLoading.value
        }

        setContent {
            ZooTheme{
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    welcomeScreenViewModel.Init(context)

                    var startScreen = Routes.Categories.route
                    if (!welcomeScreenViewModel.isFinished.value){
                        startScreen = Routes.Welcome.route
                    }

                    Navigation(startScreen)
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
