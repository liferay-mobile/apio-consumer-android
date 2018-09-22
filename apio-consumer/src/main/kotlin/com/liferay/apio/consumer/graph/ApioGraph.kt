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

package com.liferay.apio.consumer.graph

import com.liferay.apio.consumer.model.Thing
import com.liferay.apio.consumer.model.merge

/**
 * @author Paulo Cruz
 */
object ApioGraph {
    internal val graph: MutableMap<String, Node> = mutableMapOf()

    operator fun ApioGraph.get(id: String): Node? {
        return graph[id]
    }

    internal fun updateNodes(thing: Thing, embeddedThings: Map<String, Thing?>) {
        val nodes = embeddedThings.map { (id, embeddedThing) ->
            val previousThing = graph[id]?.value

            val newThing = embeddedThing?.merge(previousThing) ?: previousThing

            id to Node(id, newThing)
        }

        graph[thing.id] = Node(thing.id, thing)
        graph.putAll(nodes)
    }
}

class Node(val id: String, var value: Thing? = null)
