package com.example.qiitaclient

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.example.qiitaclient.domain.model.ArticleWithTag
import com.example.qiitaclient.domain.repository.ArticleListRepository
import com.example.qiitaclient.presentation.viewmodel.ArticleListViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArticleListViewModelUnitTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    @RelaxedMockK
    private lateinit var articleListRepository: ArticleListRepository

    @InjectMockKs
    private lateinit var viewModel: ArticleListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
        every { articleListRepository.getArticleList() } returns liveData {
            listOf<ArticleWithTag>(
                mockk(),
                mockk(),
                mockk()
            )
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun init_success() = runTest {
        viewModel.init()
        advanceUntilIdle()

        val observer = Observer<List<ArticleWithTag>> {
            assertThat(it.size).isEqualTo(3)
        }
        viewModel.articleList.observeForever(observer)
        viewModel.articleList.removeObserver(observer)
    }

    @Test
    fun init_failure() = runTest {
        coEvery { articleListRepository.refreshArticleList(any()) } throws RuntimeException()
        viewModel.init()
        advanceUntilIdle()

        val observer = Observer<Boolean> {
            assertThat(it).isTrue()
        }
        viewModel.isError.observeForever(observer)
        viewModel.isError.removeObserver(observer)
    }

    @Test
    fun onRefresh_success() = runTest {
        val result = mutableListOf<Boolean>()
        val observer = Observer<Boolean> {
            result.add(it)
        }
        viewModel.refreshing.observeForever(observer)
        viewModel.onRefresh()
        advanceUntilIdle()

        viewModel.refreshing.removeObserver(observer)
        assertThat(result[1]).isTrue()
        assertThat(result[2]).isFalse()
    }

    @Test
    fun onConsumeError() = runTest {
        val result = mutableListOf<Boolean>()
        val observer = Observer<Boolean> {
            println(it)
            result.add(it)
        }
        viewModel.isError.observeForever(observer)
        coEvery { articleListRepository.refreshArticleList(any()) } throws RuntimeException()
        viewModel.init()
        advanceUntilIdle()
        viewModel.onConsumeError()

        viewModel.isError.removeObserver(observer)
        assertThat(result[1]).isTrue()
        assertThat(result[2]).isFalse()
    }
}
