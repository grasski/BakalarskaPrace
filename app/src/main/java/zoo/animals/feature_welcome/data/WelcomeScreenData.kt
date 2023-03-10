package zoo.animals.feature_welcome.data

import android.content.Context
import zoo.animals.R
import zoo.animals.UiTexts

sealed class WelcomeScreenData(
    val image: Int,
    val title: String,
    val description: String,
) {
    class First(context: Context) : WelcomeScreenData(
        image = R.raw.image_detecting,
        title = UiTexts.ArrayResource(R.array.page_one, 0).asString(context),
        description = UiTexts.ArrayResource(R.array.page_one, 1).asString(context)
    )

    class Second(context: Context) : WelcomeScreenData(
        image = R.drawable.icon_possible_to_see,
        title = UiTexts.ArrayResource(R.array.page_two, 0).asString(context),
        description = UiTexts.ArrayResource(R.array.page_two, 1).asString(context)
    )

    class Third(context: Context) : WelcomeScreenData(
        image = R.raw.discovery,
        title = UiTexts.ArrayResource(R.array.page_three, 0).asString(context),
        description = UiTexts.ArrayResource(R.array.page_three, 1).asString(context)
    )
}
