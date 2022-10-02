package zoo.animals.feature_discovery.zoos.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.model.Place

data class InfoState (
    var place: MutableState<Place?> = mutableStateOf(null),
    var loading: MutableState<Boolean> = mutableStateOf(true),
    var error: MutableState<ApiException?> = mutableStateOf(null)
)
