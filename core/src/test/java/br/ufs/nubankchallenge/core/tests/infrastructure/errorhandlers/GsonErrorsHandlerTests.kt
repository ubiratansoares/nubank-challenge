package br.ufs.nubankchallenge.core.tests.infrastructure.errorhandlers

import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError
import br.ufs.nubankchallenge.core.infrastructure.errorhandlers.GsonErrorsHandler
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test

/**
 *
 * Created by @ubiratanfsoares
 *
 */
class GsonErrorsHandlerTests {

    lateinit var gsonErrorHandler: GsonErrorsHandler

    @Before fun `before each test`() {
        gsonErrorHandler = GsonErrorsHandler()
    }

    @Test fun `should handle interrupted json reading error`() {
        val broken = Observable.error<Any>(JsonIOException("Cannot read file"))

        broken.compose(gsonErrorHandler)
                .test()
                .assertError { error -> error is InfrastructureError.UndesiredResponse }
    }

    @Test fun `should handle json sintax error`() {
        val broken = Observable.error<Any>(JsonSyntaxException("Json should not have comments"))

        broken.compose(gsonErrorHandler)
                .test()
                .assertError { error -> error is InfrastructureError.UndesiredResponse }
    }

    @Test fun `sould handle json parsing error`() {
        val broken = Observable.error<Any>(JsonParseException("Failed to parse object"))

        broken.compose(gsonErrorHandler)
                .test()
                .assertError { error -> error is InfrastructureError.UndesiredResponse }
    }

    @Test fun `should not handle other errors`() {
        val fuck = IllegalAccessError("FUCK")
        val broken = Observable.error<String>(fuck)

        broken.compose(gsonErrorHandler)
                .test()
                .assertError { throwable -> throwable == fuck }
    }
}