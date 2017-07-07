package com.liferay.vulcan.consumer.screens.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.delegates.bindNonNull
import com.liferay.vulcan.consumer.delegates.converter
import com.liferay.vulcan.consumer.model.BlogPosting
import com.liferay.vulcan.consumer.model.Thing

class BlogPostingView(context: Context, attrs: AttributeSet) : BaseView(context, attrs) {

    val headline by bindNonNull<TextView>(R.id.headline)

    override var thing: Thing? by converter<BlogPosting> {
        headline.text = it.headline
    }

}