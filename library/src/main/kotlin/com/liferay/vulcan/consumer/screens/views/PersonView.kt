package com.liferay.vulcan.consumer.screens.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.delegates.converter
import com.liferay.vulcan.consumer.extensions.mediumFormat
import com.liferay.vulcan.consumer.model.Person
import com.liferay.vulcan.consumer.model.Thing

class PersonView(context: Context, attrs: AttributeSet) : ThingView(context, attrs) {

    val name by lazy { findViewById(R.id.person_name) as TextView }
    val email by lazy { findViewById(R.id.person_email) as TextView }
    val jobTitle by lazy { findViewById(R.id.person_job_title) as TextView }
    val birthDate by lazy { findViewById(R.id.person_birthDate) as TextView }

    override var thing: Thing? by converter<Person> {
        name.text = it.name
        email.text = it.email
        jobTitle.text = it.jobTitle
        birthDate.text = it.birthDate?.mediumFormat()
    }
}
