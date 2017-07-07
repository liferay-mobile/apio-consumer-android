package com.liferay.vulcan.consumer.delegates

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> observeNonNull(onChange: (T) -> Unit): ReadWriteProperty<Any, T?> = ObserveDelegate(onChange)

@PublishedApi
internal class ObserveDelegate<T>(val onChange: (T) -> Unit) : ReadWriteProperty<Any, T?> {

    private var t: T? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): T? = t

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        t = value

        value?.apply(onChange)
    }

}