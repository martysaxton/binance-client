package io.xooxo.binance

import io.xooxo.binance.impl.BinanceClientImpl
import io.xooxo.binance.responses.DepthResponse

fun main() {
//    val client = BinanceClientImpl(BinanceClient.Type.LIVE)
//    val book = DepthResponse()
//    client.setCloseListener { println("websocket closed") }
//    client.setAggTradeListener {
//        println("trade: $it")
//    }
////    client.setInstrumentInfoListeners(
////            { println("instrument info snapshot:  $it") },
////            { println("instrument info delta: $it") }
////    )
//    client.setDepthListeners(
//            {
//                book.applySnapshot(it)
//                printBook(book)
//            },
//            {
//                book.applyDelta(it)
//                printBook(book)
//            }
//    )
//    client.setConnectListener {
//        println("websocket connected")
//        client.subscribeToTrades()
////        client.subscribeToInstrumentInfo("BTCUSD")
//        client.subscribeToOrderBook("BTCUSD")
//    }
//    client.connectWebSocket()
}

//fun printBook(book: DepthResponse) {
//    val bids = book.getBids()
//    val asks = book.getAsks()
//    val firstBid = bids[0]
//    val firstAsk = asks[0]
//    println("bids bids=${bids.size} size=asks${asks.size}  fb=$firstBid fa=$firstAsk")
//}
