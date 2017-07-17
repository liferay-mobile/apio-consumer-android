package com.liferay.vulcan.consumer.screens.views

import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.events.Event
import com.liferay.vulcan.consumer.screens.ThingScreenlet
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

/**
 * @author Alejandro Hernández
 */
interface BaseView : AnkoLogger {

    var screenlet: ThingScreenlet?

    fun <T> sendEvent(event: Event<T>): T? = screenlet?.onEventFor(event)

    var thing: Thing?

    fun showError(message: String?) {
        error { "Error loading the thing " + message }
    }

}