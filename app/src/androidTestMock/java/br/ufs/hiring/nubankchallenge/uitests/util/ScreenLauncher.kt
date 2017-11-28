package br.ufs.hiring.nubankchallenge.uitests.util

import android.app.Activity
import android.content.Intent
import android.support.test.rule.ActivityTestRule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.plugins.RxJavaPlugins
import kotlin.reflect.KClass



/**
 *
 * Created by @ubiratanfsoares
 *
 */

class ScreenLauncher<T : Activity>(klass: KClass<T>) :
        ActivityTestRule<T>(klass.java, false, false) {

    init {
        DisableSystemAnimations()
    }

    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()
        RxJavaPlugins.setIoSchedulerHandler { AndroidSchedulers.mainThread() }
    }

    fun startScreen() {
        launchActivity(Intent())
    }
}