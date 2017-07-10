package com.liferay.vulcan.consumer.model

import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.screens.Scenario
import com.liferay.vulcan.consumer.screens.Scenario.DETAIL

data class Collection(val members: List<Thing>?, val totalItems: Int?, val pages: Pages?) {
    companion object {
        val DefaultViews: Map<Scenario, Int> = mapOf(
            DETAIL to R.layout.collection_default
        )
    }
}

data class Pages(val next: String?)