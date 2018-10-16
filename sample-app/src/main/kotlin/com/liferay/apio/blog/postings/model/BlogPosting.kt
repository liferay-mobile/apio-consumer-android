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

package com.liferay.apio.blog.postings.model

import com.liferay.apio.blog.postings.R
import com.liferay.apio.consumer.model.Relation
import com.liferay.apio.blog.postings.screens.views.Detail
import com.liferay.apio.blog.postings.screens.views.Row
import com.liferay.apio.blog.postings.screens.views.Scenario
import com.liferay.apio.consumer.extensions.asDate
import com.liferay.apio.consumer.model.Thing
import com.liferay.apio.consumer.model.get
import java.util.Date

data class BlogPosting(
	val headline: String?,
	val alternativeHeadline: String?,
	val articleBody: String?,
	val creator: Relation?,
	val createDate: Date?) {

	companion object {
		val DEFAULT_VIEWS: MutableMap<Scenario, Int> =
			mutableMapOf(
				Detail to R.layout.blog_posting_detail_custom,
				Row to R.layout.blog_posting_row_custom
			)

		val converter: (Thing) -> Any = {
			BlogPosting(
				it["headline"] as? String,
				it["alternativeHeadline"] as? String,
				it["articleBody"] as? String,
				it["creator"] as? Relation,
				(it["dateCreated"] as? String)?.asDate())
		}
	}
}
