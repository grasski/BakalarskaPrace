package zoo.animals.feature_welcome.data

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import zoo.animals.PlaceApi
import zoo.animals.feature_category.data.AnimalData
import zoo.animals.feature_discovery.zoos.data.ZooData


private val Context.dataStore by preferencesDataStore("welcomeFinished")

class WelcomeScreenViewModel(): ViewModel() {

    private val _isFinished: MutableState<Boolean> = mutableStateOf(false)
    val isFinished: State<Boolean> = _isFinished

    private val _appIsLoading: MutableState<Boolean> = mutableStateOf(true)
    val appIsLoading: State<Boolean> = _appIsLoading

    @Composable
    fun Init(context: Context){
        AnimalData.init(context)
        ZooData.init(context)
        PlaceApi.init(context)

        AnimalData.UpdateAnimals()
        ZooData.UpdateZoos()

        getWelcomeScreenState(context)

        _appIsLoading.value = false
    }

    suspend fun saveWelcomeScreenState(context: Context) {
        val key = booleanPreferencesKey("welcome")

        context.dataStore.edit { zoosVisited ->
            zoosVisited[key] = true
        }
    }

    private fun getWelcomeScreenState(context: Context): Boolean {
        val key = booleanPreferencesKey("welcome")

        val preferences = runBlocking {
            withTimeoutOrNull(1000){
                context.dataStore.data.first()
            } }

        _isFinished.value = preferences?.get(key) ?: false
        return _isFinished.value
    }
}