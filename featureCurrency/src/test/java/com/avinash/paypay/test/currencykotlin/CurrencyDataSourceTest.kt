package com.avinash.paypay.test.currencykotlin

import com.avinash.paypay.test.currencykotlin.datasource.CurrencyDataSource
import com.avinash.paypay.test.currencykotlin.model.CurrencyResult
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class CurrencyDataSourceTest {

    private val lock: CountDownLatch = CountDownLatch(1)
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8081)
        CurrencyDataSource.setBaseUrl("http://localhost:8081")
    }

    @Test
    fun testLiveRates() {
        initStubs()

        CurrencyDataSource.fetchLiveRateForSourceCurrency { result ->
            if (result is CurrencyResult.Error) {
                fail("Currency Live rates API should not fail")
            }

            if (result is CurrencyResult.Value) {
                assertNotNull(result.data)
                assertEquals(26, result.data?.currencyQuotes?.size)
                val firstElement = result.data?.currencyQuotes?.get(1)
                assertEquals(86.125012, firstElement?.currencyRate?.toDouble())
                assertEquals("AFN", firstElement?.currencyCode)
                lock.countDown()
            }
        }

        lock.await(1, TimeUnit.SECONDS)
    }

    @Test
    fun testSupportedCurrencies() {
        initStubs()

        CurrencyDataSource.fetchSupportedCurrencies { result ->
            if (result is CurrencyResult.Error) {
                fail("Currency Live rates API should not fail")
            }

            if (result is CurrencyResult.Value) {
                assertNotNull(result.data)
                assertEquals(26, result.data?.currencyList?.size)
                val firstElement = result.data?.currencyList?.get(0)
                assertEquals("\"United Arab Emirates Dirham\"", firstElement?.currencyCountryName)
                assertEquals("AED", firstElement?.currencyCode)
                lock.countDown()
            }
        }

        lock.await(1, TimeUnit.SECONDS)
    }

    private fun initStubs() {
        val dispatcher: Dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when(request.path) {
                    "/live?access_key=3087c27f7666ae9ecfa67d6225a5a4e3" -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Stubs.LIVE_RATES.getResponse())
                            .addHeader("Content-Type", "application/json")
                    }

                    "/list?access_key=3087c27f7666ae9ecfa67d6225a5a4e3" -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(Stubs.SUPPORTED_CURRENCIES.getResponse())
                            .addHeader("Content-Type", "application/json")
                    }

                    else -> MockResponse().setResponseCode(404)
                }
            }
        }

        mockWebServer.dispatcher = dispatcher
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}