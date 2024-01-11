package com.milkman.demo.data


import com.milkman.demo.local.BeerDao
import com.milkman.demo.model.BeerCacheModel
import com.milkman.demo.model.BeerModel
import com.milkman.demo.model.ResultState
import com.milkman.demo.remote.NetworkService
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test


class BeerRepositoryTest {

    private val networkService = mockk<NetworkService>()
    private val cacheService = mockk<BeerDao>()

    private val beerRepository: BeerRepositoryInterface =
        BeerRepository(networkService, cacheService)

    @Test
    fun `test getBeerList success`() = runBlocking {

        val beerModelMock = BeerModel(
            id = 1,
            name = "Buzz",
            tagline = "A Real Bitter Experience.",
            description = "A light, crisp and bitter IPA brewed with English and American hops. A small batch brewed only once.",
            image_url = "https://images.punkapi.com/v2/keg.png"
        )
        coEvery { networkService.getBeerList(any()) } returns mockk {
            every { isSuccessful } returns true
            every { body() } returns listOf(beerModelMock)
        }

        coEvery { cacheService.insert(any()) } returns Unit
        coEvery { cacheService.getAllBeers() } returns emptyList()

        val result = beerRepository.getBeerList(1).first()

        assertEquals(ResultState.Success(listOf(beerModelMock)), result)
    }

    @Test
    fun `test getBeerList failure`() = runBlocking {

        coEvery { networkService.getBeerList(any()) } returns mockk {
            every { isSuccessful } returns false
            every { code() } returns 500
            every { message() } returns "Internal Server Error"
        }

        coEvery { cacheService.getAllBeers() } returns emptyList()

        val result = beerRepository.getBeerList(1).first()

        assertTrue(result is ResultState.Failure)
    }

    @Test
    fun `test getBeerList empty`() = runBlocking {

        coEvery { networkService.getBeerList(any()) } returns mockk {
            every { isSuccessful } returns true
            every { body() } returns emptyList()
        }


        val result = beerRepository.getBeerList(1).first()

        assertTrue(result is ResultState.Empty)
    }

    @Test
    fun `test getBeerList cacheService exception`() = runBlocking {

        coEvery { networkService.getBeerList(any()) } returns mockk {
            every { isSuccessful } returns false
            every { code() } returns 404
            every { message() } returns "Not Found"
        }

        coEvery { cacheService.getBeerById(any()) } coAnswers {
            throw Exception("SIMULATED : Fetch from db is failed ")
        }

        val result = beerRepository.getBeerList(1).first()

        assertTrue(result is ResultState.Failure)
    }

    @Test
    fun `test getBeerList cacheService success`() = runBlocking {

        val beerModelMock = BeerCacheModel(
            id = 1,
            name = "Buzz",
            tagline = "A Real Bitter Experience.",
            description = "A light, crisp and bitter IPA brewed with English and American hops. A small batch brewed only once.",
            imageUrl = "https://images.punkapi.com/v2/keg.png"
        )

        coEvery { networkService.getBeerList(any()) } returns mockk {
            every { isSuccessful } returns false
            every { code() } returns 404
            every { message() } returns "Not Found"
        }

        coEvery { cacheService.getAllBeers() } returns listOf(beerModelMock)

        val result = beerRepository.getBeerList(1).first()

        assertTrue(result is ResultState.Success)
    }


    @Test
    fun `test getBeerDetail success`() = runBlocking {

        val beerModelMock = BeerModel(
            id = 1,
            name = "Buzz",
            tagline = "A Real Bitter Experience.",
            description = "A light, crisp and bitter IPA brewed with English and American hops. A small batch brewed only once.",
            image_url = "https://images.punkapi.com/v2/keg.png"
        )

        coEvery { networkService.getBeerDetail(any()) } returns mockk {
            every { isSuccessful } returns true
            every { body() } returns beerModelMock
        }


        val result = beerRepository.getBeerDetail("1").first()

        assertEquals(ResultState.Success(beerModelMock), result)
    }

    @Test
    fun `test getBeerDetail failure`() = runBlocking {
        coEvery { networkService.getBeerDetail(any()) } returns mockk {
            every { isSuccessful } returns false
            every { code() } returns 404
            every { message() } returns "Not Found"
        }

        coEvery { cacheService.getBeerById(any()) } returns mockk()

        val result = beerRepository.getBeerDetail("1").first()

        assertTrue(result is ResultState.Failure)

    }

    @Test
    fun `test getBeerDetail cacheService exception`() = runBlocking {

        coEvery { networkService.getBeerDetail(any()) } returns mockk {
            every { isSuccessful } returns false
            every { code() } returns 404
            every { message() } returns "Not Found"
        }

        coEvery { cacheService.getBeerById(any()) } coAnswers {
            throw Exception("SIMULATED : Fetch from db is failed ")
        }

        val result = beerRepository.getBeerDetail("1").first()

        assertTrue(result is ResultState.Failure)
    }


    @Test
    fun `test getBeerDetail cacheService success`() = runBlocking {

        val beerModelMock = BeerCacheModel(
            id = 1,
            name = "Buzz",
            tagline = "A Real Bitter Experience.",
            description = "A light, crisp and bitter IPA brewed with English and American hops. A small batch brewed only once.",
            imageUrl = "https://images.punkapi.com/v2/keg.png"
        )

        coEvery { networkService.getBeerList(any()) } returns mockk {
            every { isSuccessful } returns false
            every { code() } returns 404
            every { message() } returns "Not Found"
        }

        coEvery { cacheService.getBeerById(any()) } returns beerModelMock

        val result = beerRepository.getBeerDetail("1").first()

        assertTrue(result is ResultState.Success)
    }

    @Test
    fun `test getBeerDetail api return null and cacheService success`() = runBlocking {

        val beerModelMock = BeerCacheModel(
            id = 1,
            name = "Buzz",
            tagline = "A Real Bitter Experience.",
            description = "A light, crisp and bitter IPA brewed with English and American hops. A small batch brewed only once.",
            imageUrl = "https://images.punkapi.com/v2/keg.png"
        )

        coEvery { networkService.getBeerDetail(any()) } returns mockk {
            every { isSuccessful } returns true
            every { body() } returns null
        }

        coEvery { cacheService.getBeerById(any()) } returns beerModelMock

        val result = beerRepository.getBeerDetail("1").first()

        assertTrue(result is ResultState.Success)
    }


    @Test
    fun `test getBeerList  exception`() = runBlocking {

        coEvery { networkService.getBeerList(any()) } coAnswers {
            throw Exception("SIMULATED : Fetch from API is failed")
        }


        val result = beerRepository.getBeerList(1).first()

        assertTrue(result is ResultState.Failure)
    }

    @Test
    fun `test getBeerDetail  exception`() = runBlocking {

        coEvery { networkService.getBeerDetail(any()) } coAnswers {
            throw Exception("SIMULATED : Fetch from API is failed")
        }


        val result = beerRepository.getBeerDetail("1").first()

        assertTrue(result is ResultState.Failure)
    }


}
