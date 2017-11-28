package br.ufs.hiring.nubankchallenge.factories

import br.ufs.nubankchallenge.core.infrastructure.rest.NubankWebService
import com.nhaarman.mockito_kotlin.mock

/**
 *
 * Created by @ubiratanfsoares
 *
 */
object WebServiceFactory {

    val webservice by lazy(LazyThreadSafetyMode.NONE) { mock<NubankWebService>() }

}