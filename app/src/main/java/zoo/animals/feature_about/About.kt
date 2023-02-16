package zoo.animals.feature_about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import zoo.animals.R
import zoo.animals.UiTexts
import zoo.animals.shared.TopBar


@Composable
fun About(navController: NavController){
    val context = LocalContext.current
    val sources = remember { UiTexts.ArrayResource(R.array.labeledSources, 0).asArray(context) }
    val uriHandler = LocalUriHandler.current

    TopBar(title = UiTexts.StringResource(R.string.aboutApp).asString(), navController = navController)
    {
        LazyColumn(Modifier.padding(bottom = 8.dp)) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = UiTexts.StringResource(R.string.introduction).asString(),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Justify
                    )

                    Image(
                        painter = painterResource(id = R.drawable.fai_logo),
                        contentDescription = "logo FAI",
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight(0.4f)
                            .clickable(
                                onClick = {
                                    uriHandler.openUri("https://fai.utb.cz/")
                                }
                            )
                    )
                }
            }

            item{
                Text(
                    text = UiTexts.StringResource(R.string.resources).asString(),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = UiTexts.StringResource(R.string.aboutImages).asString(),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Justify,
                    )
                    Image(
                        painter = painterResource(id = R.drawable.flickr_logo),
                        contentDescription = "logo Flickr",
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight(0.4f)
                            .clickable(
                                onClick = {
                                    uriHandler.openUri("https://www.flickr.com/")
                                }
                            )
                    )
                }
            }

            item {
                val mainText = remember { UiTexts.StringResource(R.string.iconsSource).asString(context) }
                val urlText = remember { UiTexts.StringResource(R.string.lottie).asString(context) }

                val color: Color = Color.Unspecified
                val style: TextStyle = LocalTextStyle.current
                val textColor = color.takeOrElse {
                    style.color.takeOrElse {
                        LocalContentColor.current
                    }
                }

                val text = remember {
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = textColor,
                                fontStyle = style.fontStyle,
                                fontSize = 18.sp,
                                fontWeight = style.fontWeight,
                                fontFamily = style.fontFamily,
                                fontFeatureSettings = style.fontFeatureSettings,
                                fontSynthesis = style.fontSynthesis,
                                letterSpacing = style.letterSpacing
                            )
                        ) {
                            append(mainText)
                        }

                        append(urlText)

                        val startIndex = mainText.length
                        val endIndex = startIndex + urlText.length

                        addStyle(
                            style = SpanStyle(
                                color = Color(0xff64B5F6),
                                fontSize = 18.sp,
                                textDecoration = TextDecoration.Underline,
                            ), start = startIndex, end = endIndex
                        )
                        addStringAnnotation(
                            tag = "URL",
                            annotation = urlText,
                            start = startIndex, end = endIndex
                        )
                    }
                }

                ClickableText(
                    text = text,
                    modifier = Modifier
                        .padding(16.dp),
                    onClick = {
                        text
                            .getStringAnnotations("URL", it, it)
                            .firstOrNull()
                            ?.let { stringAnnotation ->
                                uriHandler.openUri(stringAnnotation.item)
                            }
                    },
                    style = style,

                )
            }

            item {
                Row(horizontalArrangement = Arrangement.Center) {
                    Text(text = UiTexts.StringResource(R.string.anotherSources).asString(),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
                    )
                }
            }

            items(sources.size){
                val url = remember { urlFromString(sources[it]) }

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .fillMaxWidth()
                ){
                    Icon(Icons.Filled.FiberManualRecord, contentDescription = "Dot",
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp))

                    Text(
                        text = url,
                        maxLines = 2,
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    url
                                        .getStringAnnotations("URL", it, it)
                                        .firstOrNull()
                                        ?.let { stringAnnotation ->
                                            uriHandler.openUri(stringAnnotation.item)
                                        }
                                })
                            .padding(end = 16.dp)
                    )
                }
            }
            
            item { 
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp)
                ) {
                    Divider(Modifier.padding(bottom = 8.dp))

                    Text(text = UiTexts.StringResource(R.string.version).asString(), color = MaterialTheme.colorScheme.primary)
                    Text(text = UiTexts.StringResource(R.string.author).asString())
                    Text(text = UiTexts.StringResource(R.string.creationDate).asString())
                }
            }
            
        }
    }
}


fun urlFromString(text: String): AnnotatedString{
    val url = buildAnnotatedString {
        append(text)
        addStyle(
            style = SpanStyle(
                color = Color(0xff64B5F6),
                fontSize = 18.sp,
                textDecoration = TextDecoration.Underline,
            ), start = 0, end = text.length
        )
        addStringAnnotation(
            tag = "URL",
            annotation = text,
            start = 0, end = text.length
        )
    }

    return url
}