package com.liferay.vulcan.consumer.screens.views.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.delegates.bindNonNull
import com.liferay.vulcan.consumer.delegates.converter
import com.liferay.vulcan.consumer.extensions.md5
import com.liferay.vulcan.consumer.model.Person
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.views.ThingView
import com.squareup.picasso.Picasso

class PersonPortraitView(context: Context, attrs: AttributeSet) : ThingView(context, attrs) {

    val image_view by bindNonNull<ImageView>(R.id.image_view)

    override var thing: Thing? by converter<Person> {
        it.email
            ?.trim()
            ?.toLowerCase()
            ?.let(String::md5)
            ?.let { "https://www.gravatar.com/avatar/$it?d=retro" }
            ?.also {
                Picasso.with(context).load(it).into(image_view)
            }
    }
}
