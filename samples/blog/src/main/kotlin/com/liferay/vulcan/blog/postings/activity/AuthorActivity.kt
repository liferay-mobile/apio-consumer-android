package com.liferay.vulcan.blog.postings.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.liferay.vulcan.blog.postings.R
import com.liferay.vulcan.consumer.delegates.bindNonNull
import com.liferay.vulcan.consumer.screens.ThingScreenlet

class AuthorActivity : AppCompatActivity() {

    val thingScreenlet by bindNonNull<ThingScreenlet>(R.id.thing_screenlet)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_author)

        val id = intent.getStringExtra("id")
        thingScreenlet.load(id)
    }

}