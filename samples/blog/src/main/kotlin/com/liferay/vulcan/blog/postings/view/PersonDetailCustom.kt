package com.liferay.vulcan.blog.postings.view

import android.content.Context
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.widget.TextView
import com.liferay.vulcan.blog.postings.R
import com.liferay.vulcan.consumer.delegates.bindNonNull
import com.liferay.vulcan.consumer.delegates.converter
import com.liferay.vulcan.consumer.extensions.mediumFormat
import com.liferay.vulcan.consumer.model.Person
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.ThingScreenlet
import com.liferay.vulcan.consumer.screens.views.ThingView

class PersonDetailCustom(context: Context, attrs: AttributeSet) : ThingView(context, attrs) {

    val avatar by bindNonNull<ThingScreenlet>(R.id.person_avatar)
    val name by bindNonNull<TextView>(R.id.person_name)
    val email by bindNonNull<TextView>(R.id.person_email)
    val jobTitle by bindNonNull<TextView>(R.id.person_job_title)
    val birthDate by bindNonNull<TextView>(R.id.person_birthDate)

    override var thing: Thing? by converter<Person> {
        avatar.thing = thing
        name.text = it.name
        email.text = Html.fromHtml("<a href=\"mailto:${it.email}\">${it.email}</a>", Html.FROM_HTML_MODE_COMPACT)
        email.linksClickable = true
        email.movementMethod = LinkMovementMethod.getInstance()
        jobTitle.text = it.jobTitle
        birthDate.text = it.birthDate?.mediumFormat()
    }
}
