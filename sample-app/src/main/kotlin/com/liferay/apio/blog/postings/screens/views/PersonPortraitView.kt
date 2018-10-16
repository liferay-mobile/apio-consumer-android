package com.liferay.apio.blog.postings.screens.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.liferay.apio.blog.postings.R
import com.liferay.apio.blog.postings.delegates.bindNonNull
import com.liferay.apio.blog.postings.model.Person
import com.liferay.apio.blog.postings.screens.ThingScreenlet
import com.liferay.apio.consumer.delegates.converter
import com.liferay.apio.consumer.model.Thing
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.nio.charset.Charset
import java.security.MessageDigest

private const val SHIFTS = 4

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
			Picasso.get().load(it).into(imageView)
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
				val char1 = chars[i shr SHIFTS and 0x0f]
				builder.append("$char1$char2")
			}
		}.toString()
	}

}
