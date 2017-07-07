package com.liferay.vulcan.consumer.screens.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.model.get
import java.text.SimpleDateFormat
import java.util.*

class PersonView(context: Context, attrs: AttributeSet) : ThingView(context, attrs) {

    val name by lazy { findViewById(R.id.person_name) as TextView }
    val email by lazy { findViewById(R.id.person_email) as TextView }
    val jobTitle by lazy { findViewById(R.id.person_job_title) as TextView }
    val birthDate by lazy { findViewById(R.id.person_birthDate) as TextView }

    override var thing: Thing? = null
        set(value) {
            field = value

            value?.let {
                name.text = value["name"] as String
                email.text = value["email"] as String
                jobTitle.text = value["jobTitle"] as String

                birthDate.text = SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).parse(value["birthDate"] as String))
            }
        }
}
