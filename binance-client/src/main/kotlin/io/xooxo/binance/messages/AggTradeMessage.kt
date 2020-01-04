package io.xooxo.binance.messages

data class AggTradeEventData(
    private val e: String, // Event type
    private val E: Long, // Event time
    private val T: Long, // Trade time
    private val s: String, // symbol

    // aggTrade
    private val a: Long, // aggTradeId
    private val p: String, // trade price
    private val q: String, // trade size
    private val f: Long, // first trade id
    private val l: Long, // last trade id
    private val m: Boolean // is the buyer a market maker

) {
    fun getEventTime(): Long { return E }
    fun getTradeTime(): Long { return T }
    fun getSymbol(): String { return s }
    fun getAggTradeId(): Long  {return a}
    fun getPrice(): String { return p }
    fun getSize(): String { return q }
    fun getFirstTradeId(): Long { return f }
    fun getLastTradeId(): Long { return l }
    fun isBuyerMarketMaker(): Boolean { return m }
}

data class AggTradeMessage(val stream: String, val data: AggTradeEventData)

typealias AggTradeListener = (aggTradeMessage: AggTradeMessage) -> Unit
