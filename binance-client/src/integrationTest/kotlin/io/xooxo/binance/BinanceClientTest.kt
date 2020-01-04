package io.xooxo.binance


import io.xooxo.binance.impl.BinanceClientImpl
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class BinanceClientTest {

    companion object {
        private const val TWO_SECONDS = 2000L
        private const val ONE_SECOND = 1000L
        private const val RETRIES = 60
    }

    private val client: BinanceClient = BinanceClientImpl(BinanceClient.Type.FUTURES)

    @Test
    fun connectionTest() {
        var connectCalled = false
        client.setConnectListener { connectCalled = true }
        client.connectWebSocket()
        Thread.sleep(TWO_SECONDS)
        assertTrue(connectCalled)

        var closeCalled = false
        client.setCloseListener { closeCalled = true }
        client.close()
        Thread.sleep(TWO_SECONDS)
        assertTrue(closeCalled)
    }

    @Test
    fun trades() {
        var tradeReceived = false
        client.setAggTradeListener { tradeReceived = true }
        client.setConnectListener { client.subscribeToAggTrades("btcusdt") }
        client.connectWebSocket()
        for (i in 1..60) {
            if (tradeReceived) {
                break
            }
            Thread.sleep(ONE_SECOND)
        }
        assertTrue(tradeReceived)
    }

    @Test
    fun orderBook() {
        var depthReceived = false
        client.setDepthListener { depthReceived = true }
        client.setConnectListener { client.subscribeToDepth("btcusdt") }
        client.connectWebSocket()
        for (i in 1..RETRIES) {
            if (depthReceived) {
                break
            }
            Thread.sleep(ONE_SECOND)
        }
        assertTrue(depthReceived)
    }
}

