package com.liferay.vulcan.consumer.model

import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.screens.Detail
import com.liferay.vulcan.consumer.screens.Scenario

data class Collection(val members: List<Thing>?, val totalItems: Int?, val pages: Pages?) {
    companion object {
        val DEFAULT_VIEWS: MutableMap<Scenario, Int> = mutableMapOf(
            Detail to R.layout.collection_detail_default
        )
    }
}

data class Pages(val next: String?)