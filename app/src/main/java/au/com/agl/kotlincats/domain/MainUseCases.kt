package au.com.agl.kotlincats.domain

import au.com.agl.kotlincats.common.Callback
import au.com.agl.kotlincats.data.OwnerRepository
import au.com.agl.kotlincats.data.model.Categories
import au.com.agl.kotlincats.data.model.Owner
import au.com.agl.kotlincats.presentation.CatAdapter

class MainUseCases(private val ownerRepository: OwnerRepository): MainFacade {
    private companion object {
        private const val TYPE_CAT = "Cat"
    }
    override fun loadGroupedCats(callback: Callback<Map<String, List<String>>>) {
        ownerRepository.get(object: Callback<List<Owner>> {
            override fun onSuccess(data: List<Owner>) {

                val cats = mapOf(
                    Categories.Male.name to mutableListOf<String>(),
                    Categories.Female.name to mutableListOf()
                )
                for (item in data) {
                    item.pets?.forEach { pet ->
                        if (pet.type == TYPE_CAT) {
                            cats[item.gender]?.add(pet.name)
                        }
                    }
                }
                cats.values.forEach(MutableList<String>::sort)
                callback.onSuccess(cats)
            }

            override fun onError(error: Throwable) {
                callback.onError(error)
            }
        })
    }
}
