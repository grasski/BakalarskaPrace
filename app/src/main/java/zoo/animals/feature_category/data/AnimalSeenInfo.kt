package zoo.animals.feature_category.data


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.util.*

@Parcelize
data class AnimalSeenInfo(
    var location: String,
    var date: Date

) : Parcelable
