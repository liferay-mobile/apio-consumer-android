package com.liferay.vulcan.consumer.model

import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.screens.views.Detail
import com.liferay.vulcan.consumer.screens.views.Row
import com.liferay.vulcan.consumer.screens.views.Scenario
import java.util.*

data class BlogPosting(
    val headline: String?,
    val alternativeHeadline: String?,
    val articleBody: String?,
    val creator: Relation?,
    val createDate: Date?) {

    companion object {
        val DEFAULT_VIEWS: MutableMap<Scenario, Int> = mutableMapOf(
            Detail to R.layout.blog_posting_detail_default,
            Row to R.layout.blog_posting_row_default
        )
    }
}