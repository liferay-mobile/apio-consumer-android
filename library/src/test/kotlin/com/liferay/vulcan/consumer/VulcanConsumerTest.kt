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

package com.liferay.vulcan.consumer

import com.github.kittinunf.result.Result
import com.liferay.vulcan.consumer.model.Relation
import com.liferay.vulcan.consumer.model.Thing
import okhttp3.Credentials
import okhttp3.HttpUrl
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class VulcanConsumerTest {

	val blogCollection = "{\"totalItems\":1,\"view\":{\"last\":\"http://screens.liferay.org.es/o/api/p/blogs?page=1&per_page=30\",\"@type\":[\"PartialCollectionView\"],\"@id\":\"http://screens.liferay.org.es/o/api/p/blogs?page=1&per_page=30\",\"first\":\"http://screens.liferay.org.es/o/api/p/blogs?page=1&per_page=30\"},\"numberOfItems\":1,\"@type\":[\"Collection\"],\"members\":[{\"creator\":\"http://screens.liferay.org.es/o/api/p/people/57457\",\"articleBody\":\"<p>My Content<\\/p>\",\"@type\":[\"BlogPosting\"],\"author\":\"http://screens.liferay.org.es/o/api/p/people/57457\",\"@context\":{\"creator\":{\"@type\":\"@id\"},\"author\":{\"@type\":\"@id\"},\"comment\":{\"@type\":\"@id\"},\"aggregateRating\":{\"@type\":\"@id\"},\"group\":{\"@type\":\"@id\"}},\"alternativeHeadline\":\"My Subtitle\",\"license\":\"https://creativecommons.org/licenses/by/4.0\",\"modifiedDate\":\"2017-08-31T18:39:52+00:00\",\"comment\":\"http://screens.liferay.org.es/o/api/p/comments?id=57499&type=blogs&filterName=assetType_id\",\"@id\":\"http://screens.liferay.org.es/o/api/p/blogs/57499\",\"aggregateRating\":\"http://screens.liferay.org.es/o/api/p/aggregate-ratings/com.liferay.vulcan.liferay.portal.identifier.ClassNameClassPKIdentifier@4d2042ba\",\"headline\":\"My Title\",\"fileFormat\":\"text/html\",\"createDate\":\"2017-08-31T18:39:52+00:00\",\"group\":\"http://screens.liferay.org.es/o/api/p/groups/57459\"}],\"@id\":\"http://screens.liferay.org.es/o/api/p/blogs\",\"@context\":{\"@vocab\":\"http://schema.org\",\"Collection\":\"http://www.w3.org/ns/hydra/pagination.jsonld\"}}"
	val credentials = Credentials.basic("vulcan@liferay.com", "vulcan")

	@Test
	fun parseCreatesPairsWithRelationsTest() {

		val blogs: Pair<Thing, Map<String, Thing?>> = parse(blogCollection)

		assertNotNull(blogs)
		assertNotNull(blogs.first)
		assertEquals(listOf("Collection"), blogs.first.type)

		val attributes = blogs.first.attributes
		assertEquals(1.0, attributes["totalItems"])

		val member = ((attributes["members"] as ArrayList<*>)[0]) as Relation
		assertEquals("http://screens.liferay.org.es/o/api/p/blogs/57499", member.id)
		assertEquals("http://screens.liferay.org.es/o/api/p/blogs?page=1&per_page=30",
			(attributes["view"] as Relation).id)

		assertEquals("<p>My Content</p>", (blogs.second[member.id] as Thing).attributes["articleBody"])
	}

	@Test
	fun requestABlogFilteredByGroupId() {

		val url = HttpUrl.parse("http://screens.liferay.org.es/o/api/p/blogs?id=57459&filterName=groupId")

		val result: Result<Thing, Exception> = requestParseWaitLoop(url!!, mapOf(), listOf(), credentials)

		assertNotNull(result.component1())
		assertEquals("http://screens.liferay.org.es/o/api/p/blogs", result.component1()!!.id)
	}

}
