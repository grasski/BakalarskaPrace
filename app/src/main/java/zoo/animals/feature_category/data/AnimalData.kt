package zoo.animals.feature_category.data


import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.common.FileUtil
import zoo.animals.R
import zoo.animals.UiTexts


object AnimalData {
    private val Context.dataStore by preferencesDataStore("animalsSeen")
    suspend fun saveSeenAnimal(animalKey: String, context: Context) {
        val key = booleanPreferencesKey(animalKey)

        (allAnimalsInstance[0] + allAnimalsInstance[1] + allAnimalsInstance[2])[animalKey]?.seen = true
        context.dataStore.edit { animalsSeen ->
            animalsSeen[key] = true
        }
    }
    suspend fun removeFromSeenAnimal(animalKey: String, context: Context) {
        val key = booleanPreferencesKey(animalKey)

        (allAnimalsInstance[0] + allAnimalsInstance[1] + allAnimalsInstance[2])[animalKey]?.seen = false
        context.dataStore.edit { animalsSeen ->
            animalsSeen[key] = false
        }
    }
    suspend fun wasAnimalSeen(animalKey: String, context: Context): Boolean {
        val key = booleanPreferencesKey(animalKey)
        val preferences = context.dataStore.data.first()
        return preferences[key] ?: false
    }


    @Composable
    fun UpdateAnimals() {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        LaunchedEffect(scope) {
            scope.launch {

                (allAnimalsInstance[0] + allAnimalsInstance[1] + allAnimalsInstance[2]).forEach { entry ->
                    val seen = wasAnimalSeen(
                        entry.key,
                        context
                    ) ?: false
                    entry.value.seen = seen
                }
            }
        }
    }


    var allAnimalsInstance = mutableListOf<MutableMap<String, Animal>>()
    fun init(context: Context): MutableList<MutableMap<String, Animal>> {
        if(allAnimalsInstance.isEmpty()){
            allAnimalsInstance.add(mammals(context))
            allAnimalsInstance.add(birds(context))
            allAnimalsInstance.add(reptiles(context))
        }
        return allAnimalsInstance
    }

    fun mammals(context: Context): MutableMap<String, Animal> {

        val animalInfo: Map<List<Int>, List<List<Float>>> = mapOf(
//            R.array.Agouti to AnimalAppearance.SOUTH_AMERICA_ARGENTINA.getCoords() + AnimalAppearance.SOUTH_AMERICA_BRAZIL.getCoords() + AnimalAppearance.SOUTH_AMERICA_BOLIVIA.getCoords(),
//            R.array.Alpaca to AnimalAppearance.SOUTH_AMERICA_BOLIVIA.getCoords() + AnimalAppearance.SOUTH_AMERICA_PERU.getCoords() + AnimalAppearance.SOUTH_AMERICA_ARGENTINA.getCoords() + AnimalAppearance.SOUTH_AMERICA_CHILE.getCoords(),
//            R.array.Bison to AnimalAppearance.NORTH_AMERICA.getCoords(),
//            R.array.Beaver to AnimalAppearance.NORTH_AMERICA.getCoords() + AnimalAppearance.EUROPE.getCoords() + AnimalAppearance.SOUTH_AMERICA_PATAGONIA.getCoords(),
//            R.array.Yak to AnimalAppearance.ASIA_TIBET_NEPAL.getCoords(),
//            R.array.Badger to AnimalAppearance.EUROPE.getCoords() + AnimalAppearance.ASIA.getCoords(),
            listOf(R.array.Capybara, R.array.taxonomyCapybara) to AnimalAppearance.SOUTH_AMERICA.getCoords(),
            listOf(R.array.Kangaroo, R.array.taxonomyKangaroo) to AnimalAppearance.AUSTRALIA.getCoords(),
            listOf(R.array.Elephant, R.array.taxonomyElephant) to AnimalAppearance.AFRICA_SOUTH.getCoords() + AnimalAppearance.AFRICA_CENTER.getCoords(),
            listOf(R.array.Giraffe, R.array.taxonomyGiraffe) to AnimalAppearance.AFRICA_SOUTH.getCoords() + AnimalAppearance.AFRICA_CENTER.getCoords(),
            listOf(R.array.Gorilla, R.array.taxonomyGorilla) to AnimalAppearance.AFRICA_CENTER.getCoords(),
            listOf(R.array.Lion, R.array.taxonomyLion) to AnimalAppearance.AFRICA.getCoords(),
            listOf(R.array.Tiger, R.array.taxonomyTiger) to AnimalAppearance.ASIA_SOUTH.getCoords(),
            listOf(R.array.Zebra, R.array.taxonomyZebra) to AnimalAppearance.AFRICA_CENTER.getCoords() + AnimalAppearance.AFRICA_SOUTH.getCoords(),
            listOf(R.array.SeaLion, R.array.taxonomySeaLion) to AnimalAppearance.SOUTH_AMERICA_SEASIDE.getCoords(),
            listOf(R.array.Cat, R.array.taxonomyCat) to AnimalAppearance.WORLD.getCoords(),
            listOf(R.array.Dog, R.array.taxonomyDog) to AnimalAppearance.WORLD.getCoords(),

//            R.array.Chinchilla to AnimalAppearance.SOUTH_AMERICA_CHILE.getCoords() + AnimalAppearance.SOUTH_AMERICA_BOLIVIA.getCoords() + AnimalAppearance.SOUTH_AMERICA_PERU.getCoords(),
//            R.array.EuropeanFallowDeer to AnimalAppearance.EUROPE.getCoords(),
//            R.array.Porcupine to AnimalAppearance.AFRICA_CENTER.getCoords(),
//            R.array.Cheetah to AnimalAppearance.AFRICA.getCoords() + AnimalAppearance.ASIA_SOUTH.getCoords(),
//            R.array.Hedgehog to AnimalAppearance.AFRICA_CENTER.getCoords(),
//            R.array.Goat to AnimalAppearance.WORLD.getCoords(),
//            R.array.Lemur to AnimalAppearance.MADAGASCAR.getCoords(),
//            R.array.AsianBlackBear to AnimalAppearance.ASIA_HIMALAYAS.getCoords(),
//            R.array.Degu to AnimalAppearance.SOUTH_AMERICA_CHILE.getCoords(),
//            R.array.Sheep to AnimalAppearance.WORLD.getCoords(),
//            R.array.Meerkat to AnimalAppearance.AFRICA_SOUTH.getCoords(),
//            R.array.Camel to AnimalAppearance.ASIA_MONGOLIA.getCoords() + AnimalAppearance.ASIA_WEST.getCoords(),
            listOf(R.array.Jaguar, R.array.taxonomyJaguar) to AnimalAppearance.CENTRAL_AMERICA.getCoords() + AnimalAppearance.SOUTH_AMERICA.getCoords(),
            listOf(R.array.Rhino, R.array.taxonomyRhino) to AnimalAppearance.AFRICA_SOUTH.getCoords(),
            listOf(R.array.Anteater, R.array.taxonomyAnteater) to AnimalAppearance.CENTRAL_AMERICA.getCoords() + AnimalAppearance.SOUTH_AMERICA.getCoords()
        )
        val animalImages: List<List<Int>> = listOf(
//            listOf(R.drawable.agouti_preview, R.drawable.agouti_main),
//            listOf(R.drawable.alpaka_preview, R.drawable.alpaka_main),
//            listOf(R.drawable.bison_preview, R.drawable.bison_main),
//            listOf(R.drawable.beaver_preview, R.drawable.beaver_main),
//            listOf(R.drawable.yak_preview, R.drawable.yak_main),
//            listOf(R.drawable.badger_preview, R.drawable.badger_main),
            listOf(R.drawable.capybara_preview, R.drawable.capybara_main),
            listOf(R.drawable.kangaroo_preview, R.drawable.kangaroo_main),
            listOf(R.drawable.elephant_preview, R.drawable.elephant_main),
            listOf(R.drawable.giraffe_preview, R.drawable.giraffe_main),
            listOf(R.drawable.gorilla_preview, R.drawable.gorilla_main),
            listOf(R.drawable.lion_preview, R.drawable.lion_main),
            listOf(R.drawable.tiger_preview, R.drawable.tiger_main),
            listOf(R.drawable.zebra_preview, R.drawable.zebra_main),
            listOf(R.drawable.sealion_preview, R.drawable.sealion_main),

            listOf(R.drawable.cat_preview, R.drawable.cat_main),
            listOf(R.drawable.dog_preview, R.drawable.dog_main),

//            listOf(R.drawable.chinchilla_preview, R.drawable.chinchilla_main),
//            listOf(R.drawable.europeanfallowdeer_preview, R.drawable.europeanfallowdeer_main),
//            listOf(R.drawable.porcupine_preview, R.drawable.porcupine_main),
//            listOf(R.drawable.cheetah_preview, R.drawable.cheetah_main),
//            listOf(R.drawable.hedgehog_preview, R.drawable.hedgehog_main),
//            listOf(R.drawable.goat_preview, R.drawable.goat_main),
//            listOf(R.drawable.lemur_preview, R.drawable.lemur_main),
//            listOf(R.drawable.asianblackbear_preview, R.drawable.asianblackbear_main),
//            listOf(R.drawable.degu_preview, R.drawable.degu_main),
//            listOf(R.drawable.sheep_preview, R.drawable.sheep_main),
//            listOf(R.drawable.meerkat_preview, R.drawable.meerkat_main),
//            listOf(R.drawable.camel_preview, R.drawable.camel_main),
            listOf(R.drawable.jaguar_preview, R.drawable.jaguar_main),
            listOf(R.drawable.rhino_preview, R.drawable.rhino_main),
            listOf(R.drawable.anteater_preview, R.drawable.anteater_main),
        )

        return animalsToList(
            context,
            UiTexts.ArrayResource(R.array.animalCategories, 0).asString(context),
            animalInfo,
            animalImages
        )
    }

    fun birds(context: Context): MutableMap<String, Animal> {

        val animalInfo: Map<List<Int>, List<List<Float>>> = mapOf(
            listOf(R.array.Flamingo, R.array.taxonomyFlamingo) to AnimalAppearance.AFRICA.getCoords() + AnimalAppearance.MADAGASCAR.getCoords() + AnimalAppearance.ASIA_SOUTH.getCoords() + AnimalAppearance.EUROPE_SOUTH.getCoords(),
            listOf(R.array.Duck, R.array.taxonomyDuck) to AnimalAppearance.WORLD.getCoords(),
//            R.array.Agapornis to AnimalAppearance.AFRICA.getCoords(),
//            R.array.Budgerigar to AnimalAppearance.AUSTRALIA.getCoords(),
//            R.array.Pheasant to AnimalAppearance.WORLD.getCoords(),
//            R.array.Vanellinae to AnimalAppearance.AUSTRALIA.getCoords() + AnimalAppearance.ASIA_INDONESIA.getCoords(),
//            R.array.Emu to AnimalAppearance.AUSTRALIA.getCoords(),
//            R.array.Goose to AnimalAppearance.WORLD.getCoords(),
//            R.array.Crane to AnimalAppearance.ASIA_CENTER.getCoords() + AnimalAppearance.EUROPE_EAST.getCoords(),
//            R.array.Canary to AnimalAppearance.CANARY.getCoords(),
//            R.array.Raven to AnimalAppearance.NORTH_AMERICA.getCoords() + AnimalAppearance.EUROPE.getCoords() + AnimalAppearance.ASIA_NORTH.getCoords(),
//            R.array.Kookaburra to AnimalAppearance.EUROPE.getCoords() + AnimalAppearance.ASIA_SOUTH.getCoords() + AnimalAppearance.ASIA_INDONESIA.getCoords(),
//            R.array.SeaEagle to AnimalAppearance.ASIA_NORTH.getCoords() + AnimalAppearance.EUROPE_EAST.getCoords(),
//            R.array.MountainEagle to AnimalAppearance.NORTH_AMERICA.getCoords() + AnimalAppearance.EUROPE_EAST.getCoords() + AnimalAppearance.EUROPE_SOUTH.getCoords() + AnimalAppearance.ASIA_NORTH.getCoords(),
//            R.array.GuineaFowl to AnimalAppearance.WORLD.getCoords(),
//            R.array.Peafowl to AnimalAppearance.INDIA.getCoords(),
//            R.array.SnowyOwl to AnimalAppearance.CANADA.getCoords() + AnimalAppearance.GREENLAND_NORTH.getCoords() + AnimalAppearance.ASIA_NORTH2.getCoords() + AnimalAppearance.EUROPE_EAST.getCoords() + AnimalAppearance.EUROPE_NORTH.getCoords(),
            listOf(R.array.Penguin, R.array.taxonomyPenguin) to AnimalAppearance.PENGUIN.getCoords(),
            listOf(R.array.Parrot, R.array.taxonomyParrot) to AnimalAppearance.SOUTH_AMERICA.getCoords() + AnimalAppearance.CENTRAL_AMERICA.getCoords() + AnimalAppearance.AUSTRALIA.getCoords() + AnimalAppearance.AFRICA_SOUTH.getCoords() + AnimalAppearance.AFRICA_CENTER.getCoords() + AnimalAppearance.MADAGASCAR.getCoords() + AnimalAppearance.ASIA_SOUTH.getCoords() + AnimalAppearance.ASIA_INDONESIA.getCoords(),
        )
        val animalImages: List<List<Int>> = listOf(
            listOf(R.drawable.flamingo_preview, R.drawable.flamingo_main),
            listOf(R.drawable.duck_preview, R.drawable.duck_main),
//            listOf(R.drawable.agapornis_preview, R.drawable.agapornis_main),
//            listOf(R.drawable.budgerigar_preview, R.drawable.budgerigar_main),
//            listOf(R.drawable.pheasant_preview, R.drawable.pheasant_main),
//            listOf(R.drawable.vanellinae_preview, R.drawable.vanellinae_main),
//            listOf(R.drawable.emu_preview, R.drawable.emu_main),
//            listOf(R.drawable.goose_preview, R.drawable.goose_main),
//            listOf(R.drawable.crane_preview, R.drawable.crane_main),
//            listOf(R.drawable.canary_preview, R.drawable.canary_main),
//            listOf(R.drawable.raven_preview, R.drawable.raven_main),
//            listOf(R.drawable.kookaburra_preview, R.drawable.kookaburra_main),
//            listOf(R.drawable.seaeagle_preview, R.drawable.seaeagle_main),
//            listOf(R.drawable.mountaineagle_preview, R.drawable.mountaineagle_main),
//            listOf(R.drawable.guineafowl_preview, R.drawable.guineafowl_main),
//            listOf(R.drawable.peafowl_preview, R.drawable.peafowl_main),
//            listOf(R.drawable.snowyowl_preview, R.drawable.snowyowl_main),
            listOf(R.drawable.penguin_preview, R.drawable.penguin_main),
            listOf(R.drawable.parrot_preview, R.drawable.parrot_main),
        )

        return animalsToList(
            context,
            UiTexts.ArrayResource(R.array.animalCategories, 1).asString(context),
            animalInfo,
            animalImages
        )
    }


    fun reptiles(context: Context): MutableMap<String, Animal> {
        val animalInfo: Map<List<Int>, List<List<Float>>> = mapOf(
            listOf(R.array.Turtle, R.array.taxonomyTurtle) to AnimalAppearance.WORLD.getCoords(),
        )
        val animalImages: List<List<Int>> = listOf(
            listOf(R.drawable.turtle_preview, R.drawable.turtle_main),
        )

        return animalsToList(
            context,
            UiTexts.ArrayResource(R.array.animalCategories, 2).asString(context),
            animalInfo,
            animalImages
        )
    }


    private fun animalsToList(
        context: Context,
        category: String,
        animals: Map<List<Int>, List<List<Float>>>,
        images: List<List<Int>>
    ): MutableMap<String, Animal> {
        val animalsList: MutableMap<String, Animal> = mutableMapOf()

        val animalLabels = FileUtil.loadLabels(context, "labels.txt")

        val taxonomy = UiTexts.ArrayResource(R.array.taxonomy, 0).asArray(context)
        val taxonomyMain: MutableMap<String, String> = mutableMapOf()

        val animalsInfoGlobal = context.resources.getStringArray(R.array.animalInfo)
        when (category) {
            UiTexts.ArrayResource(R.array.animalCategories, 0).asString(context) -> {
                val taxonomyMammals = UiTexts.ArrayResource(R.array.mammalsTaxonomy, 0).asArray(context)
                for ((i, tM) in taxonomyMammals.withIndex()){
                    taxonomyMain[taxonomy[i]] = tM
                }

                // Mammal -> gets Délka ocasu
                animalsInfoGlobal[3] = animalsInfoGlobal[3].split("/")[0]
            }
            UiTexts.ArrayResource(R.array.animalCategories, 1).asString(context) -> {
                val taxonomyBirds = UiTexts.ArrayResource(R.array.birdsTaxonomy, 0).asArray(context)
                for ((i, tM) in taxonomyBirds.withIndex()){
                    taxonomyMain[taxonomy[i]] = tM
                }

                // Bird -> gets Rozpětí křídel
                animalsInfoGlobal[3] = animalsInfoGlobal[3].split("/")[1]
            }
            else -> {
                val taxonomyReptiles = UiTexts.ArrayResource(R.array.reptilesTaxonomy, 0).asArray(context)
                for ((i, tM) in taxonomyReptiles.withIndex()){
                    taxonomyMain[taxonomy[i]] = tM
                }

            }
        }
        val originalSpecialAnimalInfo = animalsInfoGlobal[3]

        animals.onEachIndexed{ i, (animalID, appearance) ->
            val animalArray = UiTexts.ArrayResource(animalID[0], 0).asArray(context)
            val id = context.resources.getResourceEntryName(animalID[0])

            val animalTaxonomy = UiTexts.ArrayResource(animalID[1],0).asArray(context)

            val name = animalArray[0]
            val description = animalArray[animalArray.lastIndex]
            val imagesList = images[i]

            val infos = mutableListOf<String>()
            for (info in animalArray.subList(2, animalArray.lastIndex)) {
                infos.add(info)
            }

            var canDetect = false
            if (id in animalLabels){
                canDetect = true
            }

            if (":" in infos[3]) {
                val specialInfo = infos[3].split(":")
                animalsInfoGlobal[3] = specialInfo[0]
                infos[3] = specialInfo[1]
            } else {
                animalsInfoGlobal[3] = originalSpecialAnimalInfo
            }

            animalsList[id] = Animal(
                name = name,
                category = category,
                info = animalsInfoGlobal.zip(infos).toMap(),

                taxonomyMain = taxonomyMain,
                taxonomyOrder = mapOf(taxonomy[3] to animalTaxonomy[0]),
                taxonomyFamily = mapOf(taxonomy[4] to animalTaxonomy[1]),
                taxonomyGenus = mapOf(taxonomy[5] to animalTaxonomy[2].trim().split(",").toList()),

                description = description,
                previewImage = imagesList[0],
                mainImage = imagesList[1],
                appearance = appearance,
                canDetect = canDetect
            )
        }

        val result = animalsList.toList().sortedBy { (_, value) -> value.name}.toMap()
        return result.toMutableMap()
    }
}
