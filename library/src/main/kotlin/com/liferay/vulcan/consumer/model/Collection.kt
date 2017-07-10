package com.liferay.vulcan.consumer.model

import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.screens.ViewScenario

data class Collection(val members: List<Thing>?, val totalItems: Int?, val pages: Pages?) {
    companion object {
        val DefaultViews: Map<ViewScenario, Int> = mapOf(
            ViewScenario.DETAIL to R.layout.collection_default
        )
    }
}

data class Pages(val next: String?)