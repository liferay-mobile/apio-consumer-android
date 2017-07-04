package com.liferay.vulcan.consumer.model

import com.liferay.vulcan.consumer.VulcanException

data class Relation(val id: String)

data class Context(
    val vocab: String,
    val attributeContext: Map<String, Any>
)

fun contextFrom(
    jsonObject: Map<String, Any>?, parentContext: Context?): Context? {

    return jsonObject?.let {
        val vocab = (it["@vocab"] as? String)
            ?: parentContext?.vocab
            ?: throw VulcanException("Empty Vocab")

        val attributeContexts = it.filterKeys { it != "@vocab" }

        Context(vocab, attributeContexts)
    }
}

fun Context.isId(attributeName: String): Boolean =
    (attributeContext[attributeName] as? Map<String, Any>)
        ?.let { it["@type"] }
        ?.let { it == "@id" }
        ?: false

data class Thing(
    val id: String,
    val type: List<String>,
    val attributes: Map<String, Any>,
    val name: String? = null
)

operator fun Thing.get(attribute: String): Any? = attributes[attribute]

fun Thing.merge(value: Thing?): Thing = value?.let {
    Thing(id, type, attributes + it.attributes)
} ?: this