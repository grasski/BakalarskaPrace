package zoo.animals.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import zoo.animals.UiTexts
import zoo.animals.data.Zoo
import zoo.animals.R


@Composable
fun zooScreen(navController: NavController, zoo: Zoo){
    Column(Modifier.fillMaxSize()) {
        LazyColumn(){
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.2f),
                    contentAlignment = Alignment.TopCenter
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(text = zoo.type, fontSize = 30.sp)
                        Text(text = zoo.city, fontSize = 50.sp)
                    }
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.2f),
                    contentAlignment = Alignment.TopCenter
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 16.dp, end = 16.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(text = "Základní informace:", fontSize = 25.sp)
                        Row() {
                            Text(UiTexts.ArrayResource(R.array.zooInfo, 2).asString() + ": ", fontSize = 18.sp)
                            Text(text = zoo.creationData.toString(), fontSize = 18.sp)
                        }
                        Row() {
                            Text(UiTexts.ArrayResource(R.array.zooInfo, 3).asString() + ": ", fontSize = 18.sp)
                            Text(text = zoo.areaSize, fontSize = 18.sp)
                        }
                        Row() {
                            Text(UiTexts.ArrayResource(R.array.zooInfo, 4).asString() + ": ", fontSize = 18.sp)
                            Text(text = zoo.species.toString(), fontSize = 18.sp)
                        }
                        Row() {
                            val url: AnnotatedString = buildAnnotatedString {

                                val str = zoo.www
                                append(str)
                                addStyle(
                                    style = SpanStyle(
                                        color = Color(0xff64B5F6),
                                        fontSize = 18.sp,
                                        textDecoration = TextDecoration.Underline
                                    ), start = 0, end = str.length
                                )

                                // attach a string annotation that stores a URL to the text "link"
                                addStringAnnotation(
                                    tag = "URL",
                                    annotation = "https://"+zoo.www,
                                    start = 0, end = str.length
                                )

                            }
                            val uriHandler = LocalUriHandler.current

                            Text(UiTexts.ArrayResource(R.array.zooInfo, 5).asString() + ": ", fontSize = 18.sp)
                            ClickableText(text = url, onClick = {
                                url
                                    .getStringAnnotations("URL", it, it)
                                    .firstOrNull()?.let { stringAnnotation ->
                                        uriHandler.openUri(stringAnnotation.item)
                                    }
                            })
                        }
                    }
                }
            }
        }

        val singapore = LatLng(zoo.position[0], zoo.position[1])
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(singapore, 15f)
        }
        val uiSettings = MapUiSettings(
            compassEnabled = true,
            myLocationButtonEnabled = false,
            mapToolbarEnabled = true,
            indoorLevelPickerEnabled = false
        )
        Box(modifier = Modifier.fillMaxSize()){
            GoogleMap(
                modifier = Modifier
                    .matchParentSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = uiSettings
            ){
                Marker(
                    state = MarkerState(position = singapore),
                    title = "Singapore",
                    snippet = "Marker in Singapore"
                )
            }
        }
    }
}