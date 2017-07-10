package com.liferay.vulcan.consumer.model

import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.screens.ViewScenario
import java.util.Date

data class Person(val name: String?, val email: String?, val jobTitle: String?, val birthDate: Date?) {
    companion object {
        val DefaultViews: Map<ViewScenario, Int> = mapOf(
            ViewScenario.DETAIL to R.layout.person_default
        )
    }
}
