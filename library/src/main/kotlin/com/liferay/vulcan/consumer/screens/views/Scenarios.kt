package com.liferay.vulcan.consumer.screens.views

interface Scenario {
    companion object {
        var stringToScenario: ((String) -> Scenario?)? = null
    }
}

object Detail : Scenario

object Row: Scenario

data class Custom(val name: String) : Scenario