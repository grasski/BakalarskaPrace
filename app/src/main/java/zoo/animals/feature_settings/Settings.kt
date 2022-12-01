package zoo.animals.feature_settings

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import zoo.animals.R
import zoo.animals.UiTexts
import zoo.animals.shared.TopBar

@Composable
fun Settigns(navController: NavController){
    TopBar(title = UiTexts.StringResource(R.string.settings).asString(), navController = navController)
    {
        LazyColumn() {

        }
    }
}