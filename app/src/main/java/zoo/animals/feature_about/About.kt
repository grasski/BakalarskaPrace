package zoo.animals.feature_about

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import zoo.animals.R
import zoo.animals.UiTexts
import zoo.animals.shared.TopBar


@Composable
fun About(navController: NavController){
    TopBar(title = UiTexts.StringResource(R.string.aboutApp).asString(), navController = navController)
    {
        LazyColumn() {

        }
    }
}