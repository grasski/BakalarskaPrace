package zoo.animals.screens


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import zoo.animals.*
import zoo.animals.R
import zoo.animals.data.AnimalData

class MammalsScreen: AnimalCard() {

    @SuppressLint("NotConstructor")
    @Composable
    fun MammalsScreen(navController: NavController) {
        val context = LocalContext.current
        val mammals = AnimalData.mammals(context = context)

        val mammalsKeys = remember { Utils().stringMapToIndex(mammals) }
        Column {
            TopBar(UiTexts.ArrayResource(R.array.mammalsCategory, 0).asString(), navController = navController)
            {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(150.dp),
                    content = {
                        items(mammals.size){ i ->
                            mammals[mammalsKeys[i]]?.let { PreviewAnimalCard(animal = it, navController) }
                        }
                    },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    }
}
