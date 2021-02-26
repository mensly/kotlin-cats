package au.com.agl.kotlincats.domain

import au.com.agl.kotlincats.common.Callback
import au.com.agl.kotlincats.data.OwnerRepository
import au.com.agl.kotlincats.data.model.Owner

class MainUseCases(private val ownerRepository: OwnerRepository): MainFacade {
    private companion object {
        private const val TYPE_CAT = "Cat"
    }
    override fun loadGroupedCats(callback: Callback<Map<String, List<String>>>) {
        ownerRepository.get(object: Callback<List<Owner>> {
            override fun onSuccess(data: List<Owner>) {
                val cats = mutableMapOf<String, MutableList<String>>()
                for (item in data) {
                    item.pets?.forEach { pet ->
                        if (pet.type == TYPE_CAT) {
                            if (item.gender !in cats.keys) {
                                cats[item.gender] = mutableListOf()
                            }
                            cats[item.gender]!!.add(pet.name)
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
