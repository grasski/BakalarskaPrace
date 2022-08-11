package zoo.animals.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize
import zoo.animals.R

@Parcelize
data class Animal(
    val name: String,
    val category: String,
    val info: Map<String, String>,
    val description: String,
    val previewImage: Int,
    val mainImage: Int
): Parcelable

