package zoo.animals.feature_discovery.zoos.view.zoo_screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import zoo.animals.R
import zoo.animals.UiTexts
import zoo.animals.feature_discovery.zoos.data.Zoo
import zoo.animals.feature_discovery.zoos.data.InfoScreenModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfoScreen(zoo: Zoo, viewModel: InfoScreenModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val context = LocalContext.current

    // TODO: Uncomment this once going to 'production'
    LaunchedEffect(Unit) {
        viewModel.fetchPlace(zoo)
    }

    val place = viewModel.state.place.value
    val loading = viewModel.state.loading.value

    val error = viewModel.state.error.value
    val isOpen by remember(place) { mutableStateOf(place?.isOpen) }
    val weekDays = remember(place) { mutableListOf(place?.openingHours?.weekdayText) }

    val openStatus = remember {
        UiTexts.ArrayResource(R.array.openStatus,0).asArray(context)
    }
    val errorMessages = remember {
        UiTexts.ArrayResource(R.array.errorMessages,0).asArray(context)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            Row {
                Text(
                    UiTexts.ArrayResource(R.array.zooInfo, 2).asString() + ": ",
                    fontSize = 20.sp
                )
                Text(text = zoo.creationDate.toString(), fontSize = 20.sp)
            }
            Row {
                Text(
                    UiTexts.ArrayResource(R.array.zooInfo, 3).asString() + ": ",
                    fontSize = 20.sp
                )
                Text(text = zoo.areaSize, fontSize = 20.sp)
            }
            Row {
                Text(
                    UiTexts.ArrayResource(R.array.zooInfo, 4).asString() + ": ",
                    fontSize = 20.sp
                )
                Text(text = zoo.species.toString(), fontSize = 20.sp)
            }
            Row {
                val url: AnnotatedString = remember {
                    buildAnnotatedString {
                        val str = zoo.www
                        append(str)
                        addStyle(
                            style = SpanStyle(
                                color = Color(0xff64B5F6),
                                fontSize = 20.sp,
                                textDecoration = TextDecoration.Underline
                            ), start = 0, end = str.length
                        )
                        addStringAnnotation(
                            tag = "URL",
                            annotation = "https://" + zoo.www,
                            start = 0, end = str.length
                        )
                    }
                }
                val uriHandler = LocalUriHandler.current

                Text(
                    UiTexts.ArrayResource(R.array.zooInfo, 5).asString() + ": ",
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                ClickableText(text = url, onClick = {
                    url
                        .getStringAnnotations("URL", it, it)
                        .firstOrNull()?.let { stringAnnotation ->
                            uriHandler.openUri(stringAnnotation.item)
                        }
                })
            }

            Divider(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 5.dp)
            )
        }
        item{
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        Modifier
                            .align(Alignment.Center)
                            .fillMaxSize(0.5f)
                    )
                } else if (error != null){
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        if (error.statusCode == 7){
                            Text(
                                errorMessages[0],
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 25.sp,
                                textAlign = TextAlign.Center
                            )
                        } else{
                            Text(
                                errorMessages[1],
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 25.sp
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.fetchPlace(zoo)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Text(errorMessages[2])
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .fillParentMaxHeight(0.8f)
                            .padding(start = 16.dp, end = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        stickyHeader{
                            Text(
                                text = if (isOpen == true) openStatus[0] else openStatus[1],
                                fontWeight = FontWeight.Bold,
                                color = Color.Green,
                                fontSize = 27.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
                            )
                        }
                        weekDays[0]?.size?.let {
                            items(it){ i ->
                                Text(
                                    text = weekDays[0]?.get(i).toString(),
                                    fontSize = 22.sp,
                                    maxLines = 1,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

