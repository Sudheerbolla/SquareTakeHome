package com.squaretakehome.viewmodels

import com.squaretakehome.BaseApplication
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EmployeesViewModelUnitTest {
    private lateinit var viewModel: EmployeesViewModel
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        viewModel = EmployeesViewModel(BaseApplication.getInstance())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun shouldDisplayLoaderAndDisplayEmptyData() = runBlocking {
        viewModel.updateVisibilities(0)

        assertThat(viewModel.loading?.get(), `is`(0))
        assertThat(viewModel.showEmpty?.get(), `is`(8))
        assertThat(viewModel.showData?.get(), `is`(8))
    }

    @Test
    fun shouldHideLoaderAndShowDataOnView() = runBlocking {
        viewModel.updateVisibilities(1)

        assertThat(viewModel.loading?.get(), `is`(8))
        assertThat(viewModel.showEmpty?.get(), `is`(8))
        assertThat(viewModel.showData?.get(), `is`(0))
    }

    @Test
    fun shouldHideLoaderAndDisplayErrorMessage() = runBlocking {
        viewModel.updateVisibilities(2)

        assertThat(viewModel.loading?.get(), `is`(8))
        assertThat(viewModel.showEmpty?.get(), `is`(0))
        assertThat(viewModel.showData?.get(), `is`(8))
    }
}