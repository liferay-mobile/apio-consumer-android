package com.liferay.vulcan.blog.postings.view

import android.content.Context
import android.text.Html
import android.util.AttributeSet
import android.widget.TextView
import com.liferay.vulcan.blog.postings.R
import com.liferay.vulcan.consumer.delegates.bindNonNull
import com.liferay.vulcan.consumer.delegates.converter
import com.liferay.vulcan.consumer.extensions.fullFormat
import com.liferay.vulcan.consumer.model.BlogPosting
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.events.ClickEvent
import com.liferay.vulcan.consumer.screens.ThingScreenlet
import com.liferay.vulcan.consumer.screens.views.BaseView

class BlogPostingDetailCustom(context: Context, attrs: AttributeSet) : BaseView(context, attrs) {

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
                sendEvent(ClickEvent(view, Thing(it.id, listOf("Person"), emptyMap())))?.onClick(view)
            }

            creatorDetail.load(it.id)
        }

        createDate.text = it.createDate?.fullFormat()
    }

}
