package com.liferay.vulcan.consumer.screens.views.detail

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.widget.TextView
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.delegates.bindNonNull
import com.liferay.vulcan.consumer.delegates.converter
import com.liferay.vulcan.consumer.model.BlogPosting
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.events.ClickEvent
import com.liferay.vulcan.consumer.screens.ThingScreenlet
import com.liferay.vulcan.consumer.screens.views.BaseView

class BlogPostingDetailView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : BaseView,
    RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {

    override var screenlet: ThingScreenlet? = null

    val headline by bindNonNull<TextView>(R.id.headline)
    val creator by bindNonNull<ThingScreenlet>(R.id.creator_avatar)
    val articleBody by bindNonNull<TextView>(R.id.article_body)

    override var thing: Thing? by converter<BlogPosting> {
        headline.text = it.headline

        articleBody.text = it.articleBody

        it.creator?.also {
            creator.load(it.id)

            creator.setOnClickListener { view ->
                sendEvent(ClickEvent(view, Thing(it.id, listOf("Person"), emptyMap())))?.onClick(view)
            }
        }
    }

}