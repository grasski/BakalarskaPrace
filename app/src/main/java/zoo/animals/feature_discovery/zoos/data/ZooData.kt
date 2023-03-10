package zoo.animals.feature_discovery.zoos.data

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import zoo.animals.R
import zoo.animals.UiTexts


object ZooData {
    private val Context.dataStore by preferencesDataStore("zoosVisited")
    suspend fun saveVisitedZoo(zooKey: String, context: Context) {
        val key = booleanPreferencesKey(zooKey)

        allZoosInstance[zooKey]?.visited = true
        context.dataStore.edit { zoosVisited ->
            zoosVisited[key] = true
        }
    }
    suspend fun removeFromVisitedZoo(zooKey: String, context: Context) {
        val key = booleanPreferencesKey(zooKey)

        allZoosInstance[zooKey]?.visited = false
        context.dataStore.edit { zoosVisited ->
            zoosVisited[key] = false
        }
    }
    private suspend fun wasZooVisited(zooKey: String, context: Context): Boolean? {
        val key = booleanPreferencesKey(zooKey)
        val preferences = context.dataStore.data.first()

        return preferences[key]
    }

    var allZoosInstance = mutableStateMapOf<String, Zoo>()
    fun init(context: Context): SnapshotStateMap<String, Zoo> {
        if(allZoosInstance.isEmpty()){
            allZoosInstance = getZoos(context)
        }
        return allZoosInstance
    }

    private fun getZoos(context: Context): SnapshotStateMap<String, Zoo> {
        var zooId = listOf(
            R.array.Praha,
            R.array.DvurKralove,
            R.array.Liberec,
            R.array.Ostrava,
            R.array.Plzen,
            R.array.UstiNadLabem,
            R.array.Olomouc,
            R.array.Hluboka,
            R.array.Jihlava,
            R.array.Decin,
            R.array.Brno,
            R.array.Hodonin,
            R.array.Chleby,
            R.array.Zlin
        )

        val zooLogo = listOf(
            R.drawable.praha_logo,
            R.drawable.dvur_kralove_logo,
            R.drawable.liberec_logo,
            R.drawable.ostrava_logo,
            R.drawable.plzen_logo,
            R.drawable.usti_logo,
            R.drawable.olomouc_logo,
            R.drawable.hluboka_logo,
            R.drawable.jihlava_logo,
            R.drawable.decin_logo,
            R.drawable.brno_logo,
            R.drawable.hodonin_logo,
            R.drawable.chleby_logo,
            R.drawable.zlin_logo
        )

        val zoos = mutableStateMapOf<String, Zoo>()

        zooId.forEachIndexed{i, id ->
            val pos = UiTexts.ArrayResource(id, 6).asString(context).split(", ")
            val latitude = pos[0].toDouble()
            val longitude = pos[1].toDouble()
            val position = listOf(latitude, longitude)

            zoos[context.resources.getResourceEntryName(id)] = Zoo(
                UiTexts.ArrayResource(id, 0).asString(context),
                UiTexts.ArrayResource(id, 1).asString(context),
                zooLogo[i],
                UiTexts.ArrayResource(id, 2).asString(context).toInt(),
                UiTexts.ArrayResource(id, 3).asString(context),
                UiTexts.ArrayResource(id, 4).asString(context).toInt(),
                UiTexts.ArrayResource(id, 5).asString(context),
                false,
                position,
                UiTexts.ArrayResource(id, 7).asString(context)
            )

        }

        return zoos
    }


    @Composable
    fun UpdateZoos() {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        LaunchedEffect(scope) {
            scope.launch {
                allZoosInstance.forEach { entry ->
                    val visited = wasZooVisited(
                        entry.key,
                        context
                    ) ?: false
                    entry.value.visited = visited
                }
            }
        }
    }
}