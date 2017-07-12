package com.liferay.vulcan.consumer.model

import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.screens.Custom
import com.liferay.vulcan.consumer.screens.Detail
import com.liferay.vulcan.consumer.screens.Scenario
import com.liferay.vulcan.consumer.screens.ViewInfo
import java.util.Date

data class Person(val name: String?, val email: String?, val jobTitle: String?, val birthDate: Date?) {
    companion object {
        val DEFAULT_VIEWS: MutableMap<Scenario, ViewInfo> = mutableMapOf(
            Detail to ViewInfo(R.layout.person_detail_default),
            Custom("portrait") to ViewInfo(R.layout.person_portrait_default)
        )
    }
}
