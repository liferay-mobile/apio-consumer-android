package com.liferay.vulcan.consumer.screens.views.detail

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.widget.TextView
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.delegates.bindNonNull
import com.liferay.vulcan.consumer.delegates.converter
import com.liferay.vulcan.consumer.extensions.mediumFormat
import com.liferay.vulcan.consumer.model.Person
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.ThingScreenlet
import com.liferay.vulcan.consumer.screens.views.BaseView

class PersonDetailView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : BaseView,
    RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {

    override var screenlet: ThingScreenlet? = null

    val name by bindNonNull<TextView>(R.id.person_name)
    val email by bindNonNull<TextView>(R.id.person_email)
    val jobTitle by bindNonNull<TextView>(R.id.person_job_title)
    val birthDate by bindNonNull<TextView>(R.id.person_birthDate)

    override var thing: Thing? by converter<Person> {
        name.text = it.name
        email.text = it.email
        jobTitle.text = it.jobTitle
        birthDate.text = it.birthDate?.mediumFormat()
    }
}
