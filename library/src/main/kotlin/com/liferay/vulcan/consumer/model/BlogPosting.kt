package com.liferay.vulcan.consumer.model

import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.screens.ViewScenario
import com.liferay.vulcan.consumer.screens.ViewScenario.DETAIL

data class BlogPosting(val headline: String?, val creator: Relation?) {
    companion object {
        val DefaultViews: Map<ViewScenario, Int> = mapOf(
            DETAIL to R.layout.blog_posting_default
        )
    }
}