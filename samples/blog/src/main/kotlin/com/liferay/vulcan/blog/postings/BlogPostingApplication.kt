package com.liferay.vulcan.blog.postings

import android.app.Application
import com.liferay.vulcan.consumer.model.BlogPosting
import com.liferay.vulcan.consumer.model.Collection
import com.liferay.vulcan.consumer.model.Person
import com.liferay.vulcan.consumer.screens.Custom
import com.liferay.vulcan.consumer.screens.Detail
import com.liferay.vulcan.consumer.screens.Row
import com.liferay.vulcan.consumer.screens.Scenario

class BlogPostingApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        Scenario.stringToScenario = {
            if (it == "detail-small") DetailSmall else null
        }

        Person.DEFAULT_VIEWS[Custom("portrait")] = R.layout.person_portrait_custom
        Person.DEFAULT_VIEWS[DetailSmall] = R.layout.person_detail_small
        Person.DEFAULT_VIEWS[Detail] = R.layout.person_detail_custom

        BlogPosting.DEFAULT_VIEWS[Row] = R.layout.blog_posting_row_custom
        BlogPosting.DEFAULT_VIEWS[Detail] = R.layout.blog_posting_detail_custom

        Collection.DEFAULT_VIEWS[Detail] = R.layout.collection_detail_custom
    }

}

object DetailSmall : Scenario