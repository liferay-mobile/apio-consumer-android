package com.liferay.vulcan.consumer.screens.views.row

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.delegates.bindNonNull
import com.liferay.vulcan.consumer.delegates.converter
import com.liferay.vulcan.consumer.extensions.fullFormat
import com.liferay.vulcan.consumer.model.BlogPosting
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.ThingScreenlet
import com.liferay.vulcan.consumer.screens.views.BaseView

class BlogPostingRowView @JvmOverloads constructor(
	context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : BaseView,
	FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

	override var screenlet: ThingScreenlet? = null

	val headline by bindNonNull<TextView>(R.id.headline)
	val creator by bindNonNull<ThingScreenlet>(R.id.creator_avatar)
	val createDate by bindNonNull<TextView>(R.id.create_date)

	override var thing: Thing? by converter<BlogPosting> {
		headline.text = it.headline

		it.creator?.also {
			creator.load(it.id)
		}

		createDate.text = it.createDate?.fullFormat()
	}
}