package com.liferay.vulcan.consumer.model

import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.screens.Scenario
import com.liferay.vulcan.consumer.screens.Scenario.DETAIL

data class BlogPosting(val headline: String?, val creator: Relation?) {
    companion object {
        val DEFAULT_VIEWS: Map<Scenario, Int> = mapOf(
            DETAIL to R.layout.blog_posting_default
        )
    }
}