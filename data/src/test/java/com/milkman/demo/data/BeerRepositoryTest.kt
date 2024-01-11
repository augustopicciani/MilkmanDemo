package com.milkman.demo.data


import com.milkman.demo.local.BeerDao
import com.milkman.demo.model.BeerModel
import com.milkman.demo.model.ResultState
import com.milkman.demo.remote.NetworkService
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test


class BeerRepositoryTest {

    private val networkService = mockk<NetworkService>()
    private val cacheService = mockk<BeerDao>()
    private val beerRepository = BeerRepository(networkService, cacheService)

    @Test
    fun `getBeerList returns Success`() = runBlocking {

        val model = BeerModel(
            id = 1,
            name = "Buzz",
            tagline = "A Real Bitter Experience.",
            description = "A light, crisp and bitter IPA brewed with English and American hops. A small batch brewed only once.",
            image_url = "https://images.punkapi.com/v2/keg.png"
        )

        val page = 1
        val mockResponse = listOf(model)
        coEvery { networkService.getBeerList(page) } returns createMockResponseBeerList(mockResponse)
        coEvery { cacheService.insert(any()) } returns Unit

        val result = beerRepository.getBeerList(page)

        assertEquals(ResultState.Success(mockResponse), result)


    }

    @Test
    fun `getBeerDetail returns Success`() = runBlocking {

        val model = BeerModel(
            id = 1,
            name = "Buzz",
            tagline = "A Real Bitter Experience.",
            description = "A light, crisp and bitter IPA brewed with English and American hops. A small batch brewed only once.",
            image_url = "https://images.punkapi.com/v2/keg.png"
        )

        val id = "1"
        coEvery { networkService.getBeerDetail(id) } returns createMockResponseBeerDetail(model)

        val result = beerRepository.getBeerDetail(id)

        assertEquals(ResultState.Success(model), result)


    }

    @Test
    fun `getBeerList returns Empty`() = runBlocking {
        val page = 1
        coEvery { networkService.getBeerList(page) } returns createMockResponseBeerList(emptyList())

        val result = beerRepository.getBeerList(page)

        assertEquals(ResultState.Empty, result)


    }



    @Test
    fun `getBeerList returns Failure`() = runBlocking {
        val page = 1
        coEvery { networkService.getBeerList(page) } throws Exception("Mock network error")
        coEvery { cacheService.getAllBeers() } throws Exception("Mock DB error")

        val result = beerRepository.getBeerList(page)

        assertTrue(result is ResultState.Failure)

    }

    @Test
    fun `getBeerDetail returns Failure`() = runBlocking {
        val id = "1"
        coEvery { networkService.getBeerDetail(id) } throws Exception("Mock network error")
        coEvery { cacheService.getBeerById(id) } throws Exception("Mock DB error")

        val result = beerRepository.getBeerDetail(id)

        assertTrue(result is ResultState.Failure)

    }


    private fun createMockResponseBeerList(data: List<BeerModel>) = retrofit2.Response.success(data)
    private fun createMockResponseBeerDetail(data: BeerModel) = retrofit2.Response.success(data)
}

