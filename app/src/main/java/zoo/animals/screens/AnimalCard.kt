package zoo.animals.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import zoo.animals.Routes
import zoo.animals.data.Animal


open class AnimalCard {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PreviewAnimalCard(animal: Animal, navController: NavController){
        val image: Painter = painterResource(animal.previewImage)

        Card(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                .height(180.dp)
                .widthIn(150.dp, 180.dp),
            onClick = {
                navController.currentBackStackEntry?.savedStateHandle?.set("animalData", animal)
                navController.navigate(Routes.AnimalInfo.route)
            }
        ){
            Box{
                Image(
                    painter = image,
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillHeight
                )
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 350f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
                )
                Box(modifier = Modifier.align(Alignment.BottomCenter)){
                    Text(
                        text = animal.name,
                        color = Color.White,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
