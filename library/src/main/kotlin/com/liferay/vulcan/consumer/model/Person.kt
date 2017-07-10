package com.liferay.vulcan.consumer.model

import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.screens.Detail
import com.liferay.vulcan.consumer.screens.Scenario
import com.liferay.vulcan.consumer.screens.Scenario.DETAIL
import com.liferay.vulcan.consumer.screens.ViewInfo
import java.util.Date

data class Person(val name: String?, val email: String?, val jobTitle: String?, val birthDate: Date?) {
    companion object {
        val DEFAULT_VIEWS: Map<Scenario, ViewInfo> = mapOf(
            DETAIL to Detail(R.layout.person_default)
        )
    }
}
