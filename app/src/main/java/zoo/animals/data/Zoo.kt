package zoo.animals.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Zoo(
    val city: String,
    val type: String,
    val logo: Int,
    val creationData: Int,
    val areaSize: String,
    val species: Int,
    val www: String,
    var visited: Boolean,
    val position: List<Double>
): Parcelable
