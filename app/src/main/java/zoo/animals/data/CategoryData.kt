package zoo.animals.data

import android.content.Context
import zoo.animals.R
import zoo.animals.Routes
import zoo.animals.UiTexts

object CategoryData: ICategories{
    override fun mammals(context: Context): Category {
        return Category(
            UiTexts.ArrayResource(R.array.mammalsCategory, 0).asString(context),
            UiTexts.ArrayResource(R.array.mammalsCategory, 1).asString(context),
            R.drawable.mammals,
            Routes.Mammals
        )
    }

    override fun birds(context: Context): Category {
        return Category(
            UiTexts.ArrayResource(R.array.birdsCategory, 0).asString(context),
            UiTexts.ArrayResource(R.array.birdsCategory, 1).asString(context),
            R.drawable.birds2,
            Routes.Birds
        )
    }

    override fun reptiles(context: Context): Category {
        return Category(
            UiTexts.ArrayResource(R.array.reptilesCategory, 0).asString(context),
            UiTexts.ArrayResource(R.array.reptilesCategory, 1).asString(context),
            R.drawable.reptiles2,
            Routes.Reptiles
        )
    }

    override fun categoriesList(context: Context): List<Category> {
        return listOf(mammals(context), birds(context), reptiles(context))
    }
}