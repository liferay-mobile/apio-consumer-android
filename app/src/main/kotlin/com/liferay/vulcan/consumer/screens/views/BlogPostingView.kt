package com.liferay.vulcan.consumer.screens.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.model.get

class BlogPostingView(context: Context, attrs: AttributeSet) :
    ThingView(context, attrs) {

    val headline by lazy { findViewById(R.id.headline) as TextView }

    override var thing: Thing? = null
        set(value) {
            field = value

            value?.apply {
                headline.text = this["headline"] as String
            }

        }
}