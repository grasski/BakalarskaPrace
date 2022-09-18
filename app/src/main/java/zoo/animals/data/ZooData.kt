package zoo.animals.data

import android.content.Context
import zoo.animals.R
import zoo.animals.UiTexts

class ZooData {

    fun getZoos(context: Context): MutableList<Zoo> {
        val zooId = listOf(
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

        val zoos = mutableListOf<Zoo>()
        for (id in zooId){
            zoos.add(Zoo(
                UiTexts.ArrayResource(id, 0).asString(context),
                UiTexts.ArrayResource(id, 1).asString(context).toInt(),
                UiTexts.ArrayResource(id, 2).asString(context),
                UiTexts.ArrayResource(id, 3).asString(context).toInt(),
                UiTexts.ArrayResource(id, 4).asString(context)
            ))
        }

        return zoos
    }
}