package com.liferay.vulcan.consumer.delegates

import com.liferay.vulcan.consumer.model.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> converter(converter: (Thing) -> T?, onConvert: (T) -> Unit) :
    ReadWriteProperty<Any, Thing?> {

    return ThingConverterDelegate(converter, onConvert)
}

inline fun <reified T> converter(noinline onConvert: (T) -> Unit) :
    ReadWriteProperty<Any, Thing?> =
        ThingConverterDelegate({ convert<T>(it) }, onConvert)

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

inline fun <reified T> convert(thing: Thing): T? =
    convert(T::class.java, thing)

fun <T> convert(clazz: Class<T>, thing: Thing): T? {
    val t = (converters[clazz.name] as? (Thing) -> T)
        ?.invoke(thing)

    if (t == null) {
        AnkoLogger(clazz).error { "Converter not found for this class" }
    }

    return t
}

private val converters: Map<String, (Thing) -> Any> = mapOf(
    BlogPosting::class.java.name to { it: Thing ->
        BlogPosting(it["headline"] as? String)
    }
)