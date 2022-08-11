package zoo.animals

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource

sealed class UiTexts{
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ): UiTexts()

    class ArrayResource(
        @ArrayRes val resId: Int,
        val index: Int
    ): UiTexts()


    @Composable
    fun asString(): String {
        return when(this){
            is StringResource -> stringResource(resId, *args)
            is ArrayResource -> stringArrayResource(resId)[index]
        }
    }

    fun asString(context: Context): String {
        return when(this){
            is StringResource -> context.getString(resId, *args)
            is ArrayResource -> context.resources.getStringArray(resId)[index]
        }
    }

    fun asArray(context: Context): List<String> {
        return when(this){
            is ArrayResource -> context.resources.getStringArray(resId).toList()
            else -> {
                listOf<String>()}
        }
    }
}
