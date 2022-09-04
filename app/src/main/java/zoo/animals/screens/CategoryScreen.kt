package zoo.animals.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import zoo.animals.R
import zoo.animals.TopBar
import zoo.animals.UiTexts
import zoo.animals.data.Category
import zoo.animals.data.CategoryData


@Composable
fun CategoryScreen(navController: NavController){
    val context = LocalContext.current
    TopBar(title = UiTexts.StringResource(R.string.categoryTitle).asString(), navController = navController)
    {
        LazyColumn {
            item {
                CategoryCard(Arrangement.Start, CategoryData.mammals(context), navController)
            }
            item {
                CategoryCard(Arrangement.End, CategoryData.birds(context), navController)
            }
            item {
                CategoryCard(Arrangement.Start, CategoryData.reptiles(context), navController)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
    position: Arrangement.Horizontal,
    category: Category,
    navController: NavController,
) {
    val image: Painter = painterResource(id = category.imageId)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 15.dp)
            .heightIn(100.dp, 250.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = { navController.navigate(category.route.route) }
    ) {
        Row {
            if (position == Arrangement.End) {
                Box(modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.CenterVertically)
                ){
                    Text(
                        text = category.categoryName,
                        modifier = Modifier
                            .align(Alignment.Center),
                        textAlign = TextAlign.Center,
                        fontSize = 40.sp,
                    )
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = image,
                        contentDescription = category.categoryName,
                        modifier = Modifier
                            .size(145.dp)
                            .align(Alignment.Center)
                            .padding(10.dp, 10.dp)
                    )
                }
            } else{
                Box(modifier = Modifier.fillMaxWidth(0.5f)) {
                    Image(
                        painter = image,
                        contentDescription = category.categoryName,
                        modifier = Modifier
                            .size(145.dp)
                            .align(Alignment.Center)
                            .padding(10.dp, 10.dp)
                    )
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
                ){
                    Text(
                        text = category.categoryName,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 40.sp,
                    )
                }
            }
        }
    }
}
