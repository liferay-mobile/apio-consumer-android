package com.liferay.vulcan.consumer.model

import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.screens.Detail
import com.liferay.vulcan.consumer.screens.Scenario
import com.liferay.vulcan.consumer.screens.ViewInfo

data class Collection(val members: List<Thing>?, val totalItems: Int?, val pages: Pages?) {
    companion object {
        val DEFAULT_VIEWS: MutableMap<Scenario, ViewInfo> = mutableMapOf(
            Detail to ViewInfo(R.layout.collection_default)
        )
    }
}

data class Pages(val next: String?)