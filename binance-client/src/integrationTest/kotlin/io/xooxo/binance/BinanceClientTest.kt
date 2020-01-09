package io.xooxo.binance


import io.xooxo.binance.impl.BinanceClientImpl
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class BinanceClientTest {

    companion object {
        private const val RETRIES = 600
        private const val SLEEP_INTERVAL = 600L
    }

    private fun waitUntil(event: () -> Boolean): Boolean {
        for (i in 1..RETRIES) {
            if (event()) {
                return true
            }
            Thread.sleep(SLEEP_INTERVAL)
        }
        return false
    }

    @ParameterizedTest
    @EnumSource(value = BinanceClient.Type::class, names = ["SPOT", "FUTURES"])
    fun connectionTest(type: BinanceClient.Type) {
        val client: BinanceClient = BinanceClientImpl(BinanceClient.Type.FUTURES)
        var connectCalled = false
        client.setConnectListener { connectCalled = true }
        client.connectWebSocket()
        assertTrue(waitUntil { connectCalled })

        var closeCalled = false
        client.setCloseListener { closeCalled = true }
        client.close()
        assertTrue(waitUntil { closeCalled })
    }

    @ParameterizedTest
    @EnumSource(value = BinanceClient.Type::class, names = ["SPOT", "FUTURES"])
    fun trades(type: BinanceClient.Type) {
        val client: BinanceClient = BinanceClientImpl(BinanceClient.Type.FUTURES)
        var tradeReceived = false
        client.setAggTradeListener { tradeReceived = true }
        client.setConnectListener { client.subscribeToAggTrades("btcusdt") }
        client.connectWebSocket()
        assertTrue(waitUntil { tradeReceived })
    }

    @ParameterizedTest
    @EnumSource(value = BinanceClient.Type::class, names = ["SPOT", "FUTURES"])
    fun orderBook(type: BinanceClient.Type) {
        val client: BinanceClient = BinanceClientImpl(BinanceClient.Type.FUTURES)
        var depthReceived = false
        client.setDepthListener { depthReceived = true }
        client.setConnectListener { client.subscribeToDepth("btcusdt") }
        client.connectWebSocket()
        assertTrue(waitUntil { depthReceived })
    }
}

