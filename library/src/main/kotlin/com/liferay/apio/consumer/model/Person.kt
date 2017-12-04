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

package com.liferay.apio.consumer.model

import com.liferay.apio.consumer.R
import com.liferay.apio.consumer.screens.views.Custom
import com.liferay.apio.consumer.screens.views.Detail
import com.liferay.apio.consumer.screens.views.Scenario
import java.util.Date

data class Person(
	val name: String?, val email: String?, val jobTitle: String?, val birthDate: Date?, val image: String?) {

	companion object {
		val DEFAULT_VIEWS: MutableMap<Scenario, Int> =
			mutableMapOf(
				Detail to R.layout.person_detail_default,
				Custom("portrait") to R.layout.person_portrait_default
			)
	}
}
