package com.example.balo

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LifecycleObserver
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.chibatching.kotpref.Kotpref
import java.util.*

class BaloApp : MultiDexApplication(),
    Application.ActivityLifecycleCallbacks,
    LifecycleObserver {

    companion object {
        lateinit var instance: BaloApp
    }

    private var currentActivity: Activity? = null
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        Kotpref.init(this)
        instance = this
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = activity
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}
