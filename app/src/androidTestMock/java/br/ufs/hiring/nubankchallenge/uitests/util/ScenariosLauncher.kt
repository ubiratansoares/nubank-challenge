package br.ufs.hiring.nubankchallenge.uitests.util

import android.app.Activity
import android.content.Intent
import android.support.test.rule.ActivityTestRule
import br.ufs.hiring.nubankchallenge.factories.FakeWebService
import br.ufs.hiring.nubankchallenge.factories.TestScenario
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.plugins.RxJavaPlugins
import kotlin.reflect.KClass

/**
 *
 * Created by @ubiratanfsoares
 *
 */
class ScenariosLauncher<T : Activity>(klass: KClass<T>)
    : ActivityTestRule<T>(klass.java, false, false) {

    private val mockWebService by lazy { FakeWebService }

    init {
        DisableSystemAnimations()
    }

    override fun beforeActivityLaunched() {
        RxJavaPlugins.setIoSchedulerHandler { AndroidSchedulers.mainThread() }
        super.beforeActivityLaunched()
    }

    fun launchWithCenario(scenario: TestScenario) {
        mockWebService.nextState(scenario)
        launchActivity(Intent())
    }

    fun nextScenario(scenario: TestScenario) {
        mockWebService.nextState(scenario)

    }
}