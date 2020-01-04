package io.xooxo.binance

import io.xooxo.binance.impl.BinanceClientImpl

fun main() {
//    val client = BinanceClientImpl(BinanceClient.Type.FUTURES)
//    client.setCloseListener { println("websocket closed") }
//    client.setAggTradeListener { println("trade: $it") }
//    client.setInstrumentInfoListeners(
//        { println("instrument info snapshot:  $it") },
//        { println("instrument info delta: $it") }
//    )
//    client.setDepthListeners(
//        { println("depth snapshot: $it") },
//        { println("depth delta $it") }
//    )
//    client.setConnectListener {
//        println("websocket connected")
//        client.subscribeToTrades()
//        client.subscribeToInstrumentInfo("BTCUSD")
//        client.subscribeToOrderBook("BTCUSD")
//    }
//    client.connectWebSocket()
}
