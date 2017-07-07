package com.liferay.vulcan.blog.postings.viewholder

import android.view.View
import android.widget.TextView
import com.liferay.vulcan.blog.postings.R
import com.liferay.vulcan.consumer.delegates.converter
import com.liferay.vulcan.consumer.model.BlogPosting
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.adapter.ThingViewHolder
import com.liferay.vulcan.consumer.screens.views.CollectionView

class BlogPostingViewHolder(itemView: View, collectionView: CollectionView) :
    ThingViewHolder(itemView, collectionView) {

    val headline by lazy { itemView.findViewById(R.id.headline) as TextView }
    val creator by lazy { itemView.findViewById(R.id.creator) as TextView }

    override var thing: Thing? by converter<BlogPosting> {
        super.thing = this.thing

        headline.text = it.headline
        creator.text = it.creator?.id
    }
}