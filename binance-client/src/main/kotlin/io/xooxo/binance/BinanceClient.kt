package io.xooxo.binance

import io.xooxo.binance.messages.AggTradeListener
import io.xooxo.binance.messages.DepthListener
import io.xooxo.binance.responses.DepthResponse

interface BinanceClient {

    enum class Type(val baseUrl: String, val webSocketUrl: String) {
        SPOT("https://api.binance.com/api/v3", "wss://stream.binance.com:9443/stream"),
        FUTURES("https://fapi.binance.com/fapi/v1", "wss://fstream.binance.com/stream")
    }

    fun connectWebSocket()
    fun close()

    fun setConnectListener(connectListener: () -> Unit)
    fun setCloseListener(closeListener: () -> Unit)

    fun subscribeToAggTrades(symbol: String)
    fun setAggTradeListener(aggTradeListener: AggTradeListener)

    fun getDepth(symbol: String, limit: Int? = null): DepthResponse
    fun setDepthListener(depthDeltaListener: DepthListener)
    fun subscribeToDepth(symbol: String)

}
