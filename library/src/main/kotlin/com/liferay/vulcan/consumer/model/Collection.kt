package com.liferay.vulcan.consumer.model

data class Collection(val members: List<Thing>?, val totalItems: Int?, val pages: Pages?)

data class Pages(val next: String?)