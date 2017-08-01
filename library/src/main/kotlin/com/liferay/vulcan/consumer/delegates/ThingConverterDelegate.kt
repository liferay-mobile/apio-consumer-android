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

package com.liferay.vulcan.consumer.delegates

import com.liferay.vulcan.consumer.extensions.asDate
import com.liferay.vulcan.consumer.graph
import com.liferay.vulcan.consumer.model.BlogPosting
import com.liferay.vulcan.consumer.model.Collection
import com.liferay.vulcan.consumer.model.Pages
import com.liferay.vulcan.consumer.model.Person
import com.liferay.vulcan.consumer.model.Relation
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.model.get
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

private val converters: Map<String, (Thing) -> Any> = mapOf(
	BlogPosting::class.java.name to { it: Thing ->
		BlogPosting(
			it["headline"] as? String, it["alternativeHeadline"] as? String, it["articleBody"] as? String,
			it["creator"] as? Relation, (it["createDate"] as? String)?.asDate())
	},
	Collection::class.java.name to { it: Thing ->
		val members = (it["members"] as? List<Relation>)?.map {
			graph[it.id]?.value
		}?.filterNotNull()

		val totalItems = (it["totalItems"] as? Double)?.toInt()

		val nextPage = (it["view"] as Relation)["next"] as? String

		val pages = nextPage?.let(::Pages)

		Collection(members, totalItems, pages)
	},
	Person::class.java.name to { it: Thing ->
		val name = it["name"] as? String

		val email = it["email"] as? String

		val jobTitle = it["jobTitle"] as? String

		val birthDate = (it["birthDate"] as? String)?.asDate()

		Person(name, email, jobTitle, birthDate)
	}
)
