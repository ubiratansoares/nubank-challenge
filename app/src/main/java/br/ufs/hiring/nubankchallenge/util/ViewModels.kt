package br.ufs.hiring.nubankchallenge.util

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import android.view.View

/**
 *
 * Created by @ubiratanfsoares
 *
 */

inline fun <reified VM : ViewModel> FragmentActivity.screenProvider(
        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
        crossinline provider: () -> VM) = lazy(mode) {

    val factory = object : ViewModelProvider.Factory {
        override fun <Model : ViewModel> create(klass: Class<Model>) = provider() as Model
    }

    ViewModelProviders.of(this, factory).get(VM::class.java)
}

inline fun <reified VM : ViewModel> FragmentActivity.withFactory(
        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
        crossinline factory: () -> ViewModelProvider.Factory) = lazy(mode) {

    ViewModelProviders.of(this, factory()).get(VM::class.java)

}

inline fun <reified VM : ViewModel> View.screenProvider(
        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
        crossinline provider: () -> VM) = lazy(mode) {

    val factory = object : ViewModelProvider.Factory {
        override fun <Model : ViewModel> create(klass: Class<Model>) = provider() as Model
    }

    ViewModelProviders.of(this.context as FragmentActivity, factory).get(VM::class.java)
}