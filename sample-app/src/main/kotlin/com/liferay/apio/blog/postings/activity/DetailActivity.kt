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

package com.liferay.apio.blog.postings.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.liferay.apio.blog.postings.R
import com.liferay.apio.blog.postings.delegates.bindNonNull
import com.liferay.apio.consumer.model.Thing
import com.liferay.apio.blog.postings.screens.ThingScreenlet
import com.liferay.apio.blog.postings.screens.events.ScreenletEvents
import com.liferay.apio.blog.postings.screens.views.BaseView
import com.liferay.apio.blog.postings.screens.views.Detail
import org.jetbrains.anko.startActivity

class DetailActivity : AppCompatActivity(), ScreenletEvents {

	val thingScreenlet by bindNonNull<ThingScreenlet>(R.id.thing_screenlet_activity)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.thing_screenlet_activity)

		val id = intent.getStringExtra("id")

		thingScreenlet.screenletEvents = this

		thingScreenlet.load(id, scenario = Detail)
	}

	override fun <T : BaseView> onClickEvent(baseView: T, view: View, thing: Thing) = View.OnClickListener {
		startActivity<DetailActivity>("id" to thing.id)
	}

}
