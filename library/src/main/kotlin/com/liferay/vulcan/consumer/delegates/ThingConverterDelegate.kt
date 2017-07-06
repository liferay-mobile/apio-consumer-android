package com.liferay.vulcan.consumer.delegates

import com.liferay.vulcan.consumer.model.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> converter(converter: (Thing) -> T?, onConvert: (T) -> Unit) :
    ReadWriteProperty<Any, Thing?> {

    return ThingConverterDelegate(converter, onConvert)
}

@PublishedApi
internal class ThingConverterDelegate<T>(
    val converter: (Thing) -> T?,
    val onConvert: (T) -> Unit) : ReadWriteProperty<Any, Thing?> {

    private var thing: Thing? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): Thing? = thing

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Thing?) {
        thing = value

        value?.let(converter)?.apply(onConvert)
    }

}