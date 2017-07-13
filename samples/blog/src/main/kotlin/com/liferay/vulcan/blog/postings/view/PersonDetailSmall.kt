package com.liferay.vulcan.blog.postings.view

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.delegates.bindNonNull
import com.liferay.vulcan.consumer.delegates.converter
import com.liferay.vulcan.consumer.model.Person
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.views.ThingView

class PersonDetailSmall(context: Context, attrs: AttributeSet) : ThingView(context, attrs) {

    val name by bindNonNull<TextView>(R.id.person_name)

    override var thing: Thing? by converter<Person> {
        name.text = it.name
    }
}