package com.liferay.vulcan.consumer.screens.viewholder

import android.view.View
import android.widget.TextView
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.delegates.bindNonNull
import com.liferay.vulcan.consumer.delegates.converter
import com.liferay.vulcan.consumer.model.BlogPosting
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.adapter.ThingViewHolder

class BlogPostingViewHolder(itemView: View, listener: Listener) : ThingViewHolder(itemView, listener) {
    val headline by bindNonNull<TextView>(R.id.headline)
    val creator by bindNonNull<TextView>(R.id.creator)

    override var thing: Thing? by converter<BlogPosting> {
        super.thing = this.thing

        headline.text = it.headline
        creator.text = it.creator?.id
    }
}