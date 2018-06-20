/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.apio.consumer.delegates

import com.liferay.apio.consumer.model.Thing
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> converter(converter: (Thing) -> T?, onConvert: (T) -> Unit): ReadWriteProperty<Any, Thing?> =
	ThingConverterDelegate(converter, onConvert)

inline fun <reified T> converter(noinline onConvert: (T) -> Unit): ReadWriteProperty<Any, Thing?> =
	ThingConverterDelegate({ convert<T>(it) }, onConvert)

@PublishedApi
internal class ThingConverterDelegate<T>(
	val converter: (Thing) -> T?,
	val onConvert: (T) -> Unit) : ReadWriteProperty<Any, Thing?> {

	private var thing: Thing? by observeNonNull { it.let(converter)?.apply(onConvert) }

	override fun getValue(thisRef: Any, property: KProperty<*>): Thing? = thing

	override fun setValue(thisRef: Any, property: KProperty<*>, value: Thing?) {
		thing = value
	}

}

inline fun <reified T> convert(thing: Thing): T? = convert(T::class.java, thing)

fun <T> convert(clazz: Class<T>, thing: Thing): T? {
	val t = (converters[clazz.name] as? (Thing) -> T)?.invoke(thing)

	if (t == null) {
		AnkoLogger(clazz).error { "Converter not found for this class" }
	}

	return t
}

var converters: MutableMap<String, (Thing) -> Any> = mutableMapOf()