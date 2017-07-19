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

package com.liferay.vulcan.blog.postings.view

import android.content.Context
import android.text.Html
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import com.liferay.vulcan.blog.postings.R
import com.liferay.vulcan.consumer.delegates.bindNonNull
import com.liferay.vulcan.consumer.delegates.converter
import com.liferay.vulcan.consumer.extensions.fullFormat
import com.liferay.vulcan.consumer.model.BlogPosting
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.ThingScreenlet
import com.liferay.vulcan.consumer.screens.events.Event
import com.liferay.vulcan.consumer.screens.views.BaseView

class BlogPostingDetailCustom @JvmOverloads constructor(
	context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : BaseView,
	FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

	override var screenlet: ThingScreenlet? = null

	val headline by bindNonNull<TextView>(R.id.headline)
	val alternativeHeadline by bindNonNull<TextView>(R.id.alternative_headline)
	val creatorAvatar by bindNonNull<ThingScreenlet>(R.id.creator_avatar)
	val creatorDetail by bindNonNull<ThingScreenlet>(R.id.creator_detail)
	val articleBody by bindNonNull<TextView>(R.id.article_body)
	val createDate by bindNonNull<TextView>(R.id.create_date)

	override var thing: Thing? by converter<BlogPosting> {
		headline.text = it.headline

		alternativeHeadline.text = it.alternativeHeadline

		Html.fromHtml(it.articleBody, Html.FROM_HTML_MODE_COMPACT)
			.toString()
			.replace("\n", "\n\n")
			.also { articleBody.text = it }

		it.creator?.also {
			creatorAvatar.load(it.id)

			creatorAvatar.setOnClickListener { view ->
				sendEvent(Event.Click(view, Thing(it.id, listOf("Person"), emptyMap())))?.onClick(view)
			}

			creatorDetail.load(it.id)
		}

		createDate.text = it.createDate?.fullFormat()
	}

}
