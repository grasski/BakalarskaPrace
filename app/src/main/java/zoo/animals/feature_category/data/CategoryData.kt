package zoo.animals.feature_category.data

import android.content.Context
import zoo.animals.R
import zoo.animals.navigation.Routes
import zoo.animals.UiTexts

object CategoryData: ICategories {
    override fun mammals(context: Context): Category {
        return Category(
            UiTexts.ArrayResource(R.array.mammalsCategory, 0).asString(context),
            UiTexts.ArrayResource(R.array.mammalsCategory, 1).asString(context),
            R.drawable.mammals_mid,
            Routes.Mammals
        )
    }

    override fun birds(context: Context): Category {
        return Category(
            UiTexts.ArrayResource(R.array.birdsCategory, 0).asString(context),
            UiTexts.ArrayResource(R.array.birdsCategory, 1).asString(context),
            R.drawable.birds_mid,
            Routes.Birds
        )
    }

    override fun reptiles(context: Context): Category {
        return Category(
            UiTexts.ArrayResource(R.array.reptilesCategory, 0).asString(context),
            UiTexts.ArrayResource(R.array.reptilesCategory, 1).asString(context),
            R.drawable.reptiles_mid,
            Routes.Reptiles
        )
    }

    override fun categoriesList(context: Context): List<Category> {
        return listOf(mammals(context), birds(context), reptiles(context))
    }
}