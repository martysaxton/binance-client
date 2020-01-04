package io.xooxo.binance.responses

import io.xooxo.binance.messages.DepthEntry

data class DepthResponse(
    val lastUpdateId: Long,
    val bids: List<List<String>>,
    val asks: List<List<String>>
) {
    fun getBidsEntries(): List<DepthEntry> { return bids.map { DepthEntry(it[0], it[1]) } }
    fun getAskEntries(): List<DepthEntry> { return bids.map { DepthEntry(it[0], it[1]) } }
}

