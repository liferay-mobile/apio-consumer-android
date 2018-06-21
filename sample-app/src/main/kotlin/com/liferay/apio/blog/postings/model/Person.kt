package com.liferay.apio.blog.postings.model

import com.liferay.apio.blog.postings.R
import com.liferay.apio.blog.postings.screens.views.Custom
import com.liferay.apio.blog.postings.screens.views.Detail
import com.liferay.apio.blog.postings.screens.views.Scenario
import com.liferay.apio.consumer.extensions.asDate
import com.liferay.apio.consumer.model.Thing
import com.liferay.apio.consumer.model.get
import java.util.*

data class Person(
        val name: String?,
        val email: String?,
        val jobTitle: String?,
        val birthDate: Date?,
        val image: String?) {

    companion object {
        val DEFAULT_VIEWS: MutableMap<Scenario, Int> =
                mutableMapOf(
                        Detail to R.layout.person_detail_custom,
                        Custom("portrait") to R.layout.person_portrait_custom
                )

        val converter: (Thing) -> Any = { it: Thing ->

            val name = it["name"] as? String

            val email = it["email"] as? String

            val jobTitle = it["jobTitle"] as? String

            val birthDate = (it["birthDate"] as? String)?.asDate()

            val image = it["image"] as? String

            Person(name, email, jobTitle, birthDate, image)
        }
    }
}
