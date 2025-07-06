package com.telesoftas.newsapp.ui.screens.newslist

import com.telesoftas.newsapp.data.networking.response.Article
import com.telesoftas.newsapp.data.networking.response.Source
import com.telesoftas.newsapp.data.networking.response.TopHeadlinesResponse
import com.telesoftas.newsapp.data.repository.Repository
import com.telesoftas.newsapp.rules.MainDispatcherRule
import com.telesoftas.newsapp.utlis.DateFormatter
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class NewsListViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val article = Article(
        source = Source(id = "id", name = "name"),
        author = "author",
        title = "title",
        description = "description",
        url = "url",
        urlToImage = "urlToImage",
        publishedAt = "publishedAt",
        content = "content"
    )
    private val formattedDate = "formatted"
    private val response = Response.success(
        TopHeadlinesResponse(articles = listOf(article))
    )
    private val defaultRepository: Repository = mockk {
        coEvery { getTopHeadlines(any()) } returns response
    }
    private val dateFormatter: DateFormatter = mockk {
        every { formatDate(any()) } returns formattedDate
    }

    @Test
    fun onEndOfList_repositoryCalled_StateUpdated() = runTest {
        //arrange
        val articles = listOf(
            article.copy(publishedAt = formattedDate),
            article.copy(publishedAt = formattedDate),
        )
        val fixture = fixture()

        //act
        fixture.onEndOfList()

        //assert
        coVerify { defaultRepository.getTopHeadlines(any()) }
        assertEquals(articles, fixture.state.value.articles)
    }

    @Test
    fun onEndOfList_withError_StateNotUpdated() = runTest {
        // Arrange
        val stateValue = NewsListState()
        val errorRepo = mockk<Repository> {
            coEvery { getTopHeadlines(any()) } throws RuntimeException("fail")
        }
        val fixture = fixture(errorRepo)

        // Act
        fixture.onEndOfList()

        // Assert
        coVerify { errorRepo.getTopHeadlines(any()) }
        assertEquals(stateValue, fixture.state.value)
    }

    @Test
    fun onEndOfList_isSuccessfulFalse_StateNotUpdated() = runTest {
        // Arrange
        val stateValue = NewsListState()
        val unsuccessfulResponse: Response<TopHeadlinesResponse> = mockk {
            every { isSuccessful } returns false
            every { body() } returns TopHeadlinesResponse(articles = listOf(article))
        }
        val errorRepo = mockk<Repository> {
            coEvery { getTopHeadlines(any()) } returns unsuccessfulResponse
        }
        val fixture = fixture(errorRepo)

        // Act
        fixture.onEndOfList()

        // Assert
        coVerify { errorRepo.getTopHeadlines(any()) }
        assertEquals(stateValue, fixture.state.value)
    }

    @Test
    fun onRefresh_repositoryCalled_StateUpdated() = runTest {
        //arrange
        val stateValue = NewsListState(articles = listOf(article.copy(publishedAt = formattedDate)))
        val fixture = fixture()

        //act
        fixture.onRefresh()

        //assert
        coVerify { defaultRepository.getTopHeadlines(any()) }
        assertEquals(stateValue, fixture.state.value)
    }

    @Test
    fun onRefresh_withError_StateNotUpdated() = runTest {
        // Arrange
        val stateValue = NewsListState()
        val errorRepo = mockk<Repository> {
            coEvery { getTopHeadlines(any()) } throws RuntimeException("fail")
        }
        val fixture = fixture(errorRepo)

        // Act
        fixture.onRefresh()

        // Assert
        coVerify { errorRepo.getTopHeadlines(any()) }
        assertEquals(stateValue, fixture.state.value)
    }

    @Test
    fun onRefresh_isSuccessfulFalse_StateNotUpdated() = runTest {
        // Arrange
        val stateValue = NewsListState()
        val unsuccessfulResponse: Response<TopHeadlinesResponse> = mockk {
            every { isSuccessful } returns false
            every { body() } returns TopHeadlinesResponse(articles = listOf(article))
        }
        val errorRepo = mockk<Repository> {
            coEvery { getTopHeadlines(any()) } returns unsuccessfulResponse
        }
        val fixture = fixture(errorRepo)

        // Act
        fixture.onRefresh()

        // Assert
        coVerify { errorRepo.getTopHeadlines(any()) }
        assertEquals(stateValue, fixture.state.value)
    }


    private fun fixture(repository: Repository = defaultRepository) = NewsListViewModel(
        repository = repository,
        dateFormatter = dateFormatter,
    )
}