package com.liferay.vulcan.consumer.model

import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.screens.Scenario
import com.liferay.vulcan.consumer.screens.Scenario.DETAIL
import java.util.Date

data class Person(val name: String?, val email: String?, val jobTitle: String?, val birthDate: Date?) {
    companion object {
        val DEFAULT_VIEWS: Map<Scenario, Int> = mapOf(
            DETAIL to R.layout.person_default
        )
    }
}
