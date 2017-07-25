/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.vulcan.blog.postings

import android.app.Application
import com.liferay.vulcan.consumer.model.BlogPosting
import com.liferay.vulcan.consumer.model.Collection
import com.liferay.vulcan.consumer.model.Person
import com.liferay.vulcan.consumer.screens.views.Custom
import com.liferay.vulcan.consumer.screens.views.Detail
import com.liferay.vulcan.consumer.screens.views.Row
import com.liferay.vulcan.consumer.screens.views.Scenario

class BlogPostingApplication : Application() {

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
