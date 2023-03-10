package zoo.animals


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import zoo.animals.feature_category.data.Animal
import zoo.animals.feature_category.data.AnimalData


fun stringMapToIndexKey(map: Map<String, Any>): Map<Int, String> {
    val keys: Set<String> = (listOf(map.keys)[0])

    return keys.mapIndexed { index: Int, value: String -> index to value }.toMap()
}

fun String.normalize(): String {
    val original = arrayOf("ě", "é", "š", "č", "ř", "ž", "ý", "á", "í", "ú", "ů", "ó", "ň")
    val normalized = arrayOf("e", "e", "s", "c", "r", "z", "y", "a", "i", "u", "u", "o", "n")

    return this.map {
        val index = original.indexOf(it.toString())
        if (index >= 0) normalized[index] else it
    }.joinToString("")
}

fun getAnimalByName(name: String): Animal? {
    val mammals = AnimalData.allAnimalsInstance[0]
    val birds = AnimalData.allAnimalsInstance[1]
    val reptiles = AnimalData.allAnimalsInstance[2]
    val catalogAnimals = mammals + birds + reptiles

    return catalogAnimals[name]
}


@Composable
fun LineTextDivider(text: String, textAlignment: Alignment, offset: Offset) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp),
        contentAlignment = textAlignment
    ){
        var starter by rememberSaveable { mutableStateOf(false) }
        val dividerWidth: Float by animateFloatAsState(
            targetValue = if (starter) 1f else 0f,
            animationSpec = tween(800)
        )
        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = Unit){
            scope.launch {
//                delay(200)
                starter = true
            }
        }

        Divider(
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.fillMaxWidth(dividerWidth)
        )

        Box(Modifier.offset(offset.x.dp, offset.y.dp), contentAlignment = Alignment.Center){
            Text(
                text = text,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 5.dp)
            )
        }
    }
}