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

package com.liferay.vulcan.consumer.screens.views.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.delegates.bindNonNull
import com.liferay.vulcan.consumer.delegates.converter
import com.liferay.vulcan.consumer.model.Person
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.ThingScreenlet
import com.liferay.vulcan.consumer.screens.views.BaseView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.nio.charset.Charset
import java.security.MessageDigest

class PersonPortraitView @JvmOverloads constructor(
	context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseView,
	CircleImageView(context, attrs, defStyleAttr) {

	override var screenlet: ThingScreenlet? = null

	val imageView by bindNonNull<ImageView>(R.id.image_view)

	override var thing: Thing? by converter<Person> {
		val url = it.image
			?: it.email
			?.trim()
			?.toLowerCase()
			?.md5()
			?.let { "https://www.gravatar.com/avatar/$it?d=retro" }

		url?.also {
			Picasso.with(context).load(it).into(imageView)
		}
	}

	private fun String.md5(): String {
		val chars = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

		return MessageDigest.getInstance("MD5").run {
			update(this@md5.toByteArray(Charset.defaultCharset()))
			digest()
		}.let {
			it.fold(StringBuilder()) { builder, byte ->
				val i = byte.toInt()
				val char2 = chars[i and 0x0f]
				val char1 = chars[i shr 4 and 0x0f]
				builder.append("$char1$char2")
			}
		}.toString()
	}

}
