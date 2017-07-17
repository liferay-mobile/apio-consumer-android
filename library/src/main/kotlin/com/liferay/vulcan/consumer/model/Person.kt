package com.liferay.vulcan.consumer.model

import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.screens.views.Custom
import com.liferay.vulcan.consumer.screens.views.Detail
import com.liferay.vulcan.consumer.screens.views.Scenario
import java.util.Date

data class Person(val name: String?, val email: String?, val jobTitle: String?, val birthDate: Date?) {
    companion object {
        val DEFAULT_VIEWS: MutableMap<Scenario, Int> = mutableMapOf(
            Detail to R.layout.person_detail_default,
            Custom("portrait") to R.layout.person_portrait_default
        )
    }
}
