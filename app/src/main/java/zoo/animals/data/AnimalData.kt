package zoo.animals.data


import android.content.Context
import zoo.animals.R
import zoo.animals.UiTexts


object AnimalData: IAnimals {

    var allAnimalsInstance = mutableListOf<MutableMap<String, Animal>>()
    fun init(context: Context): MutableList<MutableMap<String, Animal>> {
        if(allAnimalsInstance.isEmpty()){
            allAnimalsInstance.add(mammals(context))
            allAnimalsInstance.add(birds(context))
            allAnimalsInstance.add(reptiles(context))
        }
        return allAnimalsInstance
    }

    override fun mammals(context: Context): MutableMap<String, Animal> {
        val animalInfo: List<Int> = listOf(
            R.array.Agouti,
            R.array.Alpaca,
            R.array.Bison,
            R.array.Beaver,
            R.array.Yak,
            R.array.Badger,
            R.array.Capybara,
            R.array.Kangaroo,
            R.array.Elephant,
            R.array.Giraffe,
            R.array.Gorilla,
            R.array.Lion,
            R.array.Tiger,
            R.array.Zebra,
            R.array.SeaLion,
            R.array.Cat,
            R.array.Dog,

            R.array.Chinchilla,
            R.array.EuropeanFallowDeer,
            R.array.Porcupine,
            R.array.Cheetah,
            R.array.Hedgehog,
            R.array.Goat,
            R.array.Lemur,
            R.array.AsianBlackBear,
            R.array.Degu,
            R.array.Sheep,
            R.array.Meerkat,
            R.array.Camel,
            R.array.Jaguar,
            R.array.Rhino
        )
        val animalImages: List<List<Int>> = listOf(
            listOf(R.drawable.agouti_preview, R.drawable.agouti_main),
            listOf(R.drawable.alpaka_preview, R.drawable.alpaka_main),
            listOf(R.drawable.bison_preview, R.drawable.bison_main),
            listOf(R.drawable.beaver_preview, R.drawable.beaver_main),
            listOf(R.drawable.yak_preview, R.drawable.yak_main),
            listOf(R.drawable.badger_preview, R.drawable.badger_main),
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

            listOf(R.drawable.chinchilla_preview, R.drawable.chinchilla_main),
            listOf(R.drawable.europeanfallowdeer_preview, R.drawable.europeanfallowdeer_main),
            listOf(R.drawable.porcupine_preview, R.drawable.porcupine_main),
            listOf(R.drawable.cheetah_preview, R.drawable.cheetah_main),
            listOf(R.drawable.hedgehog_preview, R.drawable.hedgehog_main),
            listOf(R.drawable.goat_preview, R.drawable.goat_main),
            listOf(R.drawable.lemur_preview, R.drawable.lemur_main),
            listOf(R.drawable.asianblackbear_preview, R.drawable.asianblackbear_main),
            listOf(R.drawable.degu_preview, R.drawable.degu_main),
            listOf(R.drawable.sheep_preview, R.drawable.sheep_main),
            listOf(R.drawable.meerkat_preview, R.drawable.meerkat_main),
            listOf(R.drawable.camel_preview, R.drawable.camel_main),
            listOf(R.drawable.jaguar_preview, R.drawable.jaguar_main),
            listOf(R.drawable.rhino_preview, R.drawable.rhino_main),
        )

        return animalsToList(
            context,
            UiTexts.ArrayResource(R.array.animalCategories, 0).asString(context),
            animalInfo,
            animalImages
        )
    }


    override fun birds(context: Context): MutableMap<String, Animal> {
        val animalInfo: List<Int> = listOf(
            R.array.Flamingo,
            R.array.Duck,
            R.array.Agapornis,
            R.array.Budgerigar,
            R.array.Pheasant,
            R.array.Vanellinae,
            R.array.Emu,
            R.array.Goose,
            R.array.Crane,
            R.array.Canary,
            R.array.Raven,
            R.array.Kookaburra,
            R.array.SeaEagle,
            R.array.MountainEagle,
            R.array.GuineaFowl,
            R.array.Peafowl,
            R.array.SnowyOwl
        )
        val animalImages: List<List<Int>> = listOf(
            listOf(R.drawable.flamingo_preview, R.drawable.flamingo_main),
            listOf(R.drawable.duck_preview, R.drawable.duck_main),
            listOf(R.drawable.agapornis_preview, R.drawable.agapornis_main),
            listOf(R.drawable.budgerigar_preview, R.drawable.budgerigar_main),
            listOf(R.drawable.pheasant_preview, R.drawable.pheasant_main),
            listOf(R.drawable.vanellinae_preview, R.drawable.vanellinae_main),
            listOf(R.drawable.emu_preview, R.drawable.emu_main),
            listOf(R.drawable.goose_preview, R.drawable.goose_main),
            listOf(R.drawable.crane_preview, R.drawable.crane_main),
            listOf(R.drawable.canary_preview, R.drawable.canary_main),
            listOf(R.drawable.raven_preview, R.drawable.raven_main),
            listOf(R.drawable.kookaburra_preview, R.drawable.kookaburra_main),
            listOf(R.drawable.seaeagle_preview, R.drawable.seaeagle_main),
            listOf(R.drawable.mountaineagle_preview, R.drawable.mountaineagle_main),
            listOf(R.drawable.guineafowl_preview, R.drawable.guineafowl_main),
            listOf(R.drawable.peafowl_preview, R.drawable.peafowl_main),
            listOf(R.drawable.snowyowl_preview, R.drawable.snowyowl_main),
        )

        return animalsToList(
            context,
            UiTexts.ArrayResource(R.array.animalCategories, 1).asString(context),
            animalInfo,
            animalImages
        )
    }


    override fun reptiles(context: Context): MutableMap<String, Animal> {
        val animalInfo: List<Int> = listOf(
            R.array.Turtle,
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


    override fun animalsToList(
        context: Context,
        category: String,
        animals: List<Int>,
        images: List<List<Int>>
    ): MutableMap<String, Animal> {
        val animalsList: MutableMap<String, Animal> = mutableMapOf()

        val animalsInfoGlobal = context.resources.getStringArray(R.array.animalInfo)
        if (category == UiTexts.ArrayResource(R.array.animalCategories, 0).asString(context)
        ) {
            // Mammal -> gets Délka ocasu
            animalsInfoGlobal[3] = animalsInfoGlobal[3].split("/")[0]
        } else if (category == UiTexts.ArrayResource(R.array.animalCategories, 1).asString(context)
        ) {
            // Bird -> gets Rozpětí křídel
            animalsInfoGlobal[3] = animalsInfoGlobal[3].split("/")[1]
        }
        val originalSpecialAnimalInfo = animalsInfoGlobal[3]

        for ((i, animalID) in animals.withIndex()) {
            val animalArray = UiTexts.ArrayResource(animalID, 0).asArray(context)
            val id = context.resources.getResourceEntryName(animalID)

            val name = animalArray[0]
            val description = animalArray[animalArray.lastIndex]
            val imagesList = images[i]

            val infos = mutableListOf<String>()
            for (info in animalArray.subList(2, animalArray.lastIndex)) {
                infos.add(info)
            }

            if (":" in infos[3]) {
                val specialInfo = infos[3].split(":")
                animalsInfoGlobal[3] = specialInfo[0]
                infos[3] = specialInfo[1]
            } else {
                animalsInfoGlobal[3] = originalSpecialAnimalInfo
            }

            animalsList[id] = Animal(
                name,
                category,
                animalsInfoGlobal.zip(infos).toMap(),
                description,
                imagesList[0],
                imagesList[1]
            )
        }

        val result = animalsList.toList().sortedBy { (_, value) -> value.name}.toMap()
        return result.toMutableMap()
    }
}
