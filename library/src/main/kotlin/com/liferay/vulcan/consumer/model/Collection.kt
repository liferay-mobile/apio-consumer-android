package com.liferay.vulcan.consumer.model

import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.screens.Detail
import com.liferay.vulcan.consumer.screens.Scenario
import com.liferay.vulcan.consumer.screens.Scenario.DETAIL
import com.liferay.vulcan.consumer.screens.ViewInfo

data class Collection(val members: List<Thing>?, val totalItems: Int?, val pages: Pages?) {
    companion object {
        val DEFAULT_VIEWS: Map<Scenario, ViewInfo> = mapOf(
            DETAIL to Detail(R.layout.collection_default)
        )
    }
}

data class Pages(val next: String?)