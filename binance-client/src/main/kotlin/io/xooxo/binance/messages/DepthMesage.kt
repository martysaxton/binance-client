package io.xooxo.binance.messages


data class DepthEventData(
    private val e: String, // Event type
    private val E: Long, // Event time
    private val T: Long, // time
    private val s: String, // symbol
    private val U: Long, // first update Id from last stream
    private val u: Long, // last update Id from last stream
    private val pu: Long, // last update Id in last stream（ie ‘u’ in last stream）
    private val b: Array<Array<String>>,
    private val a: Array<Array<String>>

) {

    fun getEventTime(): Long {
        return E
    }

    fun getTransactionTime(): Long {
        return T
    }

    fun getSymbol(): String {
        return s
    }

    //    fun getFirstUpdateId(): Long { return U }
    fun getLastUpdateId(): Long {
        return u
    }

    fun getPreviousLastUpdateId(): Long {
        return pu
    }

    fun getBidsEntries(): List<DepthEntry> {
        return b.map { DepthEntry(it[0], it[1]) }
    }

    fun getAskEntries(): List<DepthEntry> {
        return a.map { DepthEntry(it[0], it[1]) }
    }
}

data class DepthMessage(val stream: String, val data: DepthEventData)

typealias DepthListener = (depthEvent: DepthMessage) -> Unit
