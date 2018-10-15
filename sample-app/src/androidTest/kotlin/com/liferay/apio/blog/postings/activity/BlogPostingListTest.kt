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

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.WindowManager
import com.liferay.apio.blog.postings.R
import com.liferay.apio.consumer.ApioConsumer
import com.liferay.apio.consumer.authenticator.BasicAuthenticator
import okhttp3.Credentials
import okhttp3.HttpUrl
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

const val TEST_DOMAIN = "http://screens.liferay.org.es/o/api/p/"

@RunWith(AndroidJUnit4::class)
@LargeTest
class BlogPostingListTest {

	@Rule
	@JvmField
	val activityRule = ActivityTestRule(MainActivity::class.java)

	private val credentials = Credentials.basic("apio@liferay.com", "apio")

	@Before
	fun unlockScreen() {
		val activity = activityRule.getActivity()
		val wakeUpDevice = Runnable {
			activity.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
					WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
		}
		activity.runOnUiThread(wakeUpDevice)
	}

	@Test
	fun appRendersLayoutTest() {

		val view = withId(R.id.thing_screenlet_activity)

		onView(view).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
	}

	@Test
	fun requestABlogFilteredByGroupId() {

		val url = HttpUrl.parse("http://screens.liferay.org.es/o/api/p/groups/57459/blogs")

		url?.let {
			val apioConsumer = ApioConsumer(BasicAuthenticator(credentials))

			apioConsumer.fetch(url, {
				Assert.assertNotNull(it)
				Assert.assertEquals(TEST_DOMAIN + "groups/57459/blogs", it.id)
			}) {
				Assert.fail()
			}
		}
	}

	//	@Test
	fun thingScreenletRenderingBlogsShowsResultsWithTextTest() {

		appRendersLayoutTest()

		onView(withId(R.id.headline))
			.check(matches(isDisplayed()))
			.check(matches(withText("My Title")))
	}

}
