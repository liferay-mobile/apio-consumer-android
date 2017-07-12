package com.liferay.vulcan.blog.postings.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.liferay.vulcan.blog.postings.R
import com.liferay.vulcan.consumer.delegates.bindNonNull
import com.liferay.vulcan.consumer.model.Person
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.Custom
import com.liferay.vulcan.consumer.screens.ScreenletEvents
import com.liferay.vulcan.consumer.screens.ThingScreenlet
import com.liferay.vulcan.consumer.screens.ViewInfo
import com.liferay.vulcan.consumer.screens.views.BaseView
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(), ScreenletEvents {

    val thingScreenlet by bindNonNull<ThingScreenlet>(R.id.thing_screenlet)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.thing_screenlet_activity)

        val id = "http://192.168.50.33:8080/o/api/group/20143/p/blogs"

        Person.DEFAULT_VIEWS[Custom("portrait")] = ViewInfo(R.layout.person_portrait_custom)

        thingScreenlet.load(id)

        thingScreenlet.screenletEvents = this
    }

    override fun <T : BaseView> onClickEvent(baseView: T, view: View, thing: Thing) = View.OnClickListener {
        startActivity<DetailActivity>("id" to thing.id)
    }

}