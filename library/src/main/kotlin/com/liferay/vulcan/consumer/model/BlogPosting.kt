package com.liferay.vulcan.consumer.model

data class BlogPosting(val headline: String?) {
    companion object {
        val converter: (Thing) -> BlogPosting = {
            BlogPosting(it["headline"] as? String)
        }
    }
}