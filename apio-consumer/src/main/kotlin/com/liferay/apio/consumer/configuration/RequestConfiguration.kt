package com.liferay.apio.consumer.configuration

/**
 * @author Paulo Cruz
 */
class RequestConfiguration @JvmOverloads constructor(
	val embedded: List<String> = emptyList(),
	val fields: Map<String, List<String>> = emptyMap(),
	val headers: List<RequestHeader> = emptyList()) {

	constructor(vararg headers: RequestHeader? = emptyArray()) : this(emptyList(), emptyMap(), headers.filterNotNull())
}