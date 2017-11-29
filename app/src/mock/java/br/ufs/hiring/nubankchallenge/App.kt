package br.ufs.hiring.nubankchallenge

import android.app.Application
import br.ufs.hiring.nubankchallenge.mocked.webServiceSimulatedWith

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        webServiceSimulatedWith {
            allResponsesSucceed()
        }
    }
}