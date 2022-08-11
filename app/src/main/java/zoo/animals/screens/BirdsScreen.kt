package zoo.animals.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import zoo.animals.R
import zoo.animals.UiTexts
import zoo.animals.Utils
import zoo.animals.data.AnimalData
import zoo.animals.TopBar

class BirdsScreen: AnimalCard(){

    @SuppressLint("NotConstructor")
    @Composable
    fun BirdsScreen(navController: NavController) {
        val context = LocalContext.current
        val birds = AnimalData.birds(context = context)

        val birdsKeys = remember { Utils().stringMapToIndex(birds) }
        Column {
            TopBar(UiTexts.ArrayResource(R.array.birdsCategory, 0).asString(), navController = navController)
            {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(150.dp),
                    content = {
                        items(birds.size){ i ->
                            birds[birdsKeys[i]]?.let { PreviewAnimalCard(animal = it, navController) }
                        }
                    },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    }

}
