package com.liferay.vulcan.consumer.model

import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.screens.Detail
import com.liferay.vulcan.consumer.screens.Scenario
import com.liferay.vulcan.consumer.screens.Scenario.DETAIL
import com.liferay.vulcan.consumer.screens.ViewInfo

data class BlogPosting(val headline: String?, val creator: Relation?) {
    companion object {
        val DEFAULT_VIEWS: Map<Scenario, ViewInfo> = mapOf(
            DETAIL to Detail(R.layout.blog_posting_default)
        )
    }
}