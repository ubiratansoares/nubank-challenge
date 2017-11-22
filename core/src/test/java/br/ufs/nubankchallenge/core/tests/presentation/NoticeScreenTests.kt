package br.ufs.nubankchallenge.core.tests.presentation

import br.ufs.nubankchallenge.core.domain.notice.RetrieveNotice
import br.ufs.nubankchallenge.core.presentation.notice.NoticeScreen
import br.ufs.nubankchallenge.core.presentation.notice.NoticeScreenModel
import br.ufs.nubankchallenge.core.tests.util.Fixtures
import br.ufs.nubankchallenge.core.tests.util.SilentObserver
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 *
 * Created by @ubiratanfsoares
 *
 */

@RunWith(RobolectricTestRunner::class)
class NoticeScreenTests {

    private val notice by lazy { Fixtures.chargebackNotice() }

    lateinit var screen: NoticeScreen
    lateinit var usecase: RetrieveNotice

    @Before fun `before each test`() {
        usecase = mock()
        screen = NoticeScreen(usecase)

        whenever(usecase.execute())
                .thenReturn(Observable.just(notice))
    }

    @Test fun `should retrieve new screen state`() {
        val expected = NoticeScreenModel(notice)

        screen.retrieve()
                .test()
                .assertValue { it == expected }
    }

    @Test fun `should replay previous retrieved state`() {
        screen.retrieve().subscribe(SilentObserver)
        screen.retrieve().test().assertComplete()

        verify(usecase, times(1)).execute()
        verifyNoMoreInteractions(usecase)
    }


    @Test fun `should refresh when new notice demanded`() {
        screen.retrieve().subscribe(SilentObserver)
        screen.retrieve(true).subscribe(SilentObserver)

        verify(usecase, times(2)).execute()
        verifyNoMoreInteractions(usecase)
    }
}