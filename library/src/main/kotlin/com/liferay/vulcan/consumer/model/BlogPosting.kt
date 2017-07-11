package com.liferay.vulcan.consumer.model

import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.screens.Detail
import com.liferay.vulcan.consumer.screens.Row
import com.liferay.vulcan.consumer.screens.Scenario
import com.liferay.vulcan.consumer.screens.ViewInfo

data class BlogPosting(val headline: String?, val creator: Relation?) {
    companion object {
        val DEFAULT_VIEWS: MutableMap<Scenario, ViewInfo> = mutableMapOf(
            Detail to ViewInfo(R.layout.blog_posting_detail_default),
            Row to ViewInfo(R.layout.blog_posting_row_default)
        )
    }
}