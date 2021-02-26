package au.com.agl.kotlincats.data.model

import androidx.annotation.StringRes
import au.com.agl.kotlincats.R

enum class Categories(@StringRes val label: Int) {
    Male(R.string.gender_male),
    Female(R.string.gender_female),
    Cutest(R.string.category_cutest)
}