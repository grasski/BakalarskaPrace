package zoo.animals.data


import android.content.Context
import android.content.res.Resources
import zoo.animals.R
import zoo.animals.UiTexts


object AnimalData: IAnimals {
    override fun mammals(context: Context): MutableMap<String, Animal> {
        val mammalsAnimals = context.resources.getStringArray(R.array.mammals)

        val infoOfAnimals: List<Int> = listOf(
            R.array.agutiInfo,
            R.array.alpakaInfo,
            R.array.bizonInfo,
            R.array.beaverInfo,
            R.array.yakInfo,
            R.array.badgerInfo,
            R.array.capybaraInfo,
            R.array.kangarooInfo,
            R.array.elephantInfo,
            R.array.giraffeInfo,
            R.array.gorillaInfo,
            R.array.lionInfo,
            R.array.tigerInfo,
            R.array.zebraInfo,
            R.array.sealionInfo,
            R.array.catInfo,
            R.array.dogInfo,
        )
        val detailsOfAnimals: List<Int> = listOf(
            R.array.aguti,
            R.array.alpaka,
            R.array.bizon,
            R.array.beaver,
            R.array.yak,
            R.array.badger,
            R.array.capybara,
            R.array.kangaroo,
            R.array.elephant,
            R.array.giraffe,
            R.array.gorilla,
            R.array.lion,
            R.array.tiger,
            R.array.zebra,
            R.array.sealion,
            R.array.cat,
            R.array.dog,
        )
        val imagesOfAnimals: List<List<Int>> = listOf(
            listOf(R.drawable.aguti_preview, R.drawable.aguti_main),
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
        )

        return animalsToList(
            context,
            mammalsAnimals,
            infoOfAnimals,
            detailsOfAnimals,
            imagesOfAnimals
        )
    }


    override fun birds(context: Context): MutableMap<String, Animal> {
        val birdsAnimals = context.resources.getStringArray(R.array.birds)

        val infoOfAnimals: List<Int> = listOf(
            R.array.flamingoInfo,
            R.array.duckInfo
        )
        val detailsOfAnimals: List<Int> = listOf(
            R.array.flamingo,
            R.array.duck
        )
        val imagesOfAnimals: List<List<Int>> = listOf(
            listOf(R.drawable.flamingo_preview, R.drawable.flamingo_main),
            listOf(R.drawable.duck_preview, R.drawable.duck_main),
        )

        return animalsToList(
            context,
            birdsAnimals,
            infoOfAnimals,
            detailsOfAnimals,
            imagesOfAnimals
        )
    }


    override fun reptiles(context: Context): MutableMap<String, Animal> {
        val birdsAnimals = context.resources.getStringArray(R.array.reptiles)

        val infoOfAnimals: List<Int> = listOf(
            R.array.turtleInfo,
        )
        val detailsOfAnimals: List<Int> = listOf(
            R.array.turtle,
        )
        val imagesOfAnimals: List<List<Int>> = listOf(
            listOf(R.drawable.turtle_preview, R.drawable.turtle_main),
        )

        return animalsToList(
            context,
            birdsAnimals,
            infoOfAnimals,
            detailsOfAnimals,
            imagesOfAnimals
        )
    }


    override fun animalsToList(
        context: Context,
        animalCategory: Array<String>,
        infoOfAnimals: List<Int>,
        detailsOfAnimals: List<Int>,
        imagesOfAnimals: List<List<Int>>
    ): MutableMap<String, Animal> {
        val animalInfo = context.resources.getStringArray(R.array.animalInfo)

        if (animalCategory[0] in UiTexts.ArrayResource(R.array.mammals, 0).asArray(context)
        ){
            // Mammal -> gets Délka ocasu
            animalInfo[3] = animalInfo[3].split("/")[0]
        }
        else if (animalCategory[0] in UiTexts.ArrayResource(R.array.birds, 0).asArray(context)
        ){
            // Bird -> gets Rozpětí křídel
            animalInfo[3] = animalInfo[3].split("/")[1]
        }
        val originalSpecialAnimalInfo = animalInfo[3]

        val animals: MutableMap<String, Animal> = mutableMapOf()
        for (i in animalCategory.indices){
            val info = context.resources.getStringArray(infoOfAnimals[i])
            val details = context.resources.getStringArray(detailsOfAnimals[i])

            if (":" in info[3]){
                val specialInfo = info[3].split(":")
                animalInfo[3] = specialInfo[0]
                info[3] = specialInfo[1]
            } else{
                animalInfo[3] = originalSpecialAnimalInfo
            }

            val animal = Animal(
                details[0],
                details[1],
                animalInfo.zip(info).toMap(),
                details[2],
                imagesOfAnimals[i][0],
                imagesOfAnimals[i][1]
            )
            animals[animalCategory[i]] = animal
        }
        return animals
    }
}

