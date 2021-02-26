package au.com.agl.kotlincats.domain

import au.com.agl.kotlincats.common.Callback

interface MainFacade {
    fun loadGroupedCats(callback: Callback<Map<String, List<String>>>)
}