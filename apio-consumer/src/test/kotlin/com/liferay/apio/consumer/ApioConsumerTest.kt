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

package com.liferay.apio.consumer

import com.liferay.apio.consumer.exception.RequestFailedException
import com.liferay.apio.consumer.model.Relation
import com.liferay.apio.consumer.model.Thing
import com.liferay.apio.consumer.parser.ThingParser
import com.liferay.apio.consumer.request.RequestExecutor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

const val TEST_DOMAIN = "http://screens.liferay.org.es/o/api/p/"

class ApioConsumerTest {

	private val blogCollection = "{\"totalItems\":1,\"view\":{\"last\":\"http://screens.liferay.org.es/o/api/p/blogs?page=1&per_page=30\",\"@type\":[\"PartialCollectionView\"],\"@id\":\"http://screens.liferay.org.es/o/api/p/blogs?page=1&per_page=30\",\"first\":\"http://screens.liferay.org.es/o/api/p/blogs?page=1&per_page=30\"},\"numberOfItems\":1,\"@type\":[\"Collection\"],\"member\":[{\"creator\":\"http://screens.liferay.org.es/o/api/p/people/57457\",\"articleBody\":\"<p>My Content<\\/p>\",\"@type\":[\"BlogPosting\"],\"author\":\"http://screens.liferay.org.es/o/api/p/people/57457\",\"@context\":{\"creator\":{\"@type\":\"@id\"},\"author\":{\"@type\":\"@id\"},\"comment\":{\"@type\":\"@id\"},\"aggregateRating\":{\"@type\":\"@id\"},\"group\":{\"@type\":\"@id\"}},\"alternativeHeadline\":\"My Subtitle\",\"license\":\"https://creativecommons.org/licenses/by/4.0\",\"modifiedDate\":\"2017-08-31T18:39:52+00:00\",\"comment\":\"http://screens.liferay.org.es/o/api/p/comments?id=57499&type=blogs&filterName=assetType_id\",\"@id\":\"http://screens.liferay.org.es/o/api/p/blogs/57499\",\"aggregateRating\":\"http://screens.liferay.org.es/o/api/p/aggregate-ratings/com.liferay.apio.liferay.portal.identifier.ClassNameClassPKIdentifier@4d2042ba\",\"headline\":\"My Title\",\"fileFormat\":\"text/html\",\"createDate\":\"2017-08-31T18:39:52+00:00\",\"group\":\"http://screens.liferay.org.es/o/api/p/groups/57459\"}],\"@id\":\"http://screens.liferay.org.es/o/api/p/blogs\",\"@context\":{\"@vocab\":\"http://schema.org\",\"Collection\":\"http://www.w3.org/ns/hydra/pagination.jsonld\"}}"
	private val notFoundError = "{\"statusCode\": 404,\"title\": \"Resource not found\",\"@type\": \"not-found\"}"

	@Test
	fun parseCreatesPairsWithRelationsTest() {

		val blogs: Pair<Thing, Map<String, Thing?>>? = ThingParser.parse(blogCollection)

		assertNotNull(blogs)

		blogs?.let {
			assertNotNull(blogs.first)
			assertEquals(listOf("Collection"), blogs.first.type)

			val attributes = blogs.first.attributes
			assertEquals(1.0, attributes["totalItems"])

			val member = ((attributes["member"] as ArrayList<*>)[0]) as Relation
			assertEquals(TEST_DOMAIN + "blogs/57499", member.id)
			assertEquals(TEST_DOMAIN + "blogs?page=1&per_page=30",
				(attributes["view"] as Relation).id)

			assertEquals("<p>My Content</p>", (blogs.second[member.id] as Thing).attributes["articleBody"])
		}
	}

	@Test
	fun requestABlogFilteredByGroupId() {

		val mockWebServer = MockWebServer()
		val body = MockResponse()
			.addHeader("Content-Type", "application/json; charset=utf-8")
			.addHeader("Cache-Control", "no-cache")
			.setBody(blogCollection)
		mockWebServer.enqueue(body)

		val url = mockWebServer.url("o/api/p/blogs?id=57459&filterName=groupId")

		val thing = RequestExecutor.requestThing(url!!, mapOf(), listOf())

		assertNotNull(thing)
		assertEquals(TEST_DOMAIN + "blogs", thing.id)

		mockWebServer.shutdown()
	}

	@Test
	fun requestABlogFilteredByInvalidGroupId() {
		val expectedTitle = "Resource not found"
		val expectedType = listOf("not-found")
		val responseStatusCode = 404

		val mockWebServer = MockWebServer()
		val body = MockResponse()
			.addHeader("Content-Type", "application/json; charset=utf-8")
			.addHeader("Cache-Control", "no-cache")
			.setBody(notFoundError)
			.setResponseCode(responseStatusCode)
		mockWebServer.enqueue(body)

		val url = mockWebServer.url("o/api/p/blogs?id=INVALID&filterName=groupId")

		try {
			RequestExecutor.requestThing(url!!, mapOf(), listOf())
		} catch (e: RequestFailedException) {
			assertEquals(responseStatusCode, e.statusCode)
			assertEquals(expectedTitle, e.title)
			assertEquals(expectedType, e.type)
		}

		mockWebServer.shutdown()
	}

}
