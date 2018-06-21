package com.liferay.apio.blog.postings.delegates

import com.liferay.apio.blog.postings.model.BlogPosting
import com.liferay.apio.blog.postings.model.Collection
import com.liferay.apio.blog.postings.model.Person
import com.liferay.apio.consumer.delegates.converters

class ConverterDelegate {

    companion object {
        @JvmStatic
        fun initializeConverter() {
            converters[BlogPosting::class.java.name] = BlogPosting.converter
            converters[Collection::class.java.name] = Collection.converter
            converters[Person::class.java.name] = Person.converter
        }
    }
}