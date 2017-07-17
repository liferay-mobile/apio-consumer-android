package com.liferay.vulcan.blog.postings.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

import com.liferay.vulcan.blog.postings.R
import com.liferay.vulcan.consumer.delegates.bindNonNull
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.events.ScreenletEvents
import com.liferay.vulcan.consumer.screens.ThingScreenlet
import com.liferay.vulcan.consumer.screens.views.BaseView
import com.liferay.vulcan.consumer.screens.views.Detail
import org.jetbrains.anko.startActivity

class DetailActivity : AppCompatActivity(), ScreenletEvents {

    val thingScreenlet by bindNonNull<ThingScreenlet>(R.id.thing_screenlet)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.thing_screenlet_activity)

        val id = intent.getStringExtra("id")

        thingScreenlet.screenletEvents = this

        thingScreenlet.load(id, Detail)
    }

    override fun <T : BaseView> onClickEvent(baseView: T, view: View, thing: Thing) = View.OnClickListener {
        startActivity<DetailActivity>("id" to thing.id)
    }


}