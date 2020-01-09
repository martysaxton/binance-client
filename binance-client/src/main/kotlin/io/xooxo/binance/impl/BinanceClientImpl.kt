package io.xooxo.binance.impl


import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
import io.xooxo.binance.BinanceClient
import io.xooxo.binance.messages.AggTradeListener
import io.xooxo.binance.messages.AggTradeMessage
import io.xooxo.binance.messages.DepthListener
import io.xooxo.binance.messages.DepthMessage
import io.xooxo.binance.responses.DepthResponse
import org.http4k.client.WebsocketClient
import org.http4k.core.Method
import org.http4k.core.Uri
import org.http4k.format.ConfigurableJackson
import org.http4k.format.Jackson.auto
import org.http4k.format.asConfigurable
import org.http4k.format.withStandardMappings
import org.http4k.websocket.Websocket
import org.http4k.websocket.WsMessage
import org.http4k.websocket.WsStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Jackson : ConfigurableJackson(KotlinModule()
    .asConfigurable()
    .withStandardMappings()
    .done()
    .deactivateDefaultTyping()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
    .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, false)
    .configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, false)
)

class BinanceClientImpl(private val type: BinanceClient.Type) : BinanceClient {

    companion object {
        val log: Logger = LoggerFactory.getLogger(BinanceClientImpl::class.java)
        const val PING_INTERVAL = 10000L
    }

    init {
        log.debug("instantiating {}", BinanceClientImpl::class.simpleName)
    }

    private var connected = false

    private var connectListener: (() -> Unit)? = null
    private var closeListener: (() -> Unit)? = null

    private var closing = false
    private var webSocket: Websocket? = null

    private var aggTradeListener: AggTradeListener? = null
    private var depthListener: DepthListener? = null
    private var nextMessageId = 1

    private val jsonLens = WsMessage.auto<JsonNode>().toLens()

    override fun connectWebSocket() {
        log.info("connecting to {}", type.webSocketUrl)
        closing = false
        webSocket = WebsocketClient.nonBlocking(Uri.of(type.webSocketUrl)) { it ->
            it.run {
                log.info("connected to {}", type.webSocketUrl)
                connected = true
                connectListener?.let { it() }
            }
        }

        webSocket?.run {
            onMessage(::onMessage)
            onClose(::onClose)
            onError { log.error("error", it) }
        }
    }

    private fun onClose(wsStatus: WsStatus) {
        log.info("websocket closing")
        connected = false
        closeListener?.let { it() }
        if (!closing) {
            connectWebSocket()
        }
    }

    override fun close() {
        closing = true
        webSocket?.close()
    }

    override fun setConnectListener(connectListener: () -> Unit) {
        this.connectListener = connectListener
    }

    override fun setCloseListener(closeListener: () -> Unit) {
        this.closeListener = closeListener
    }

    private fun getSubscriptionMessage(symbol: String, type: String): String {
        return """{"method": "SUBSCRIBE","params":["$symbol@$type"],"id": ${nextMessageId++}}"""
    }

    override fun subscribeToAggTrades(symbol: String) {
        webSocket!!.send(WsMessage(getSubscriptionMessage(symbol.toLowerCase(), "aggTrade")))
    }

    override fun setAggTradeListener(aggTradeListener: AggTradeListener) {
        this.aggTradeListener = aggTradeListener
    }

    override fun setDepthListener(depthListener: DepthListener) {
        this.depthListener = depthListener
    }

    override fun getDepth(symbol: String, limit: Int?): DepthResponse {
//        val paramsMap = mutableMapOf("symbol" to symbol)
//        if (limit != null) {
//            paramsMap["limit"] = limit.toString()
//        }
//        val response = exec(org.http4k.core.Request(Method.GET, url("/depth", paramsMap)))
//        return orderBookLens(response)
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun subscribeToDepth(symbol: String) {
        webSocket!!.send(WsMessage(getSubscriptionMessage(symbol.toLowerCase(), "depth")))
    }

    private fun onMessage(message: WsMessage) {
        BinanceClientImpl.log.debug("websocket received: {}", message)
        val json = jsonLens(message)
        val resultNode = json["result"]
        if (resultNode != null) {
            log.debug("received result")
        } else {
            val streamNode = json["stream"]
            if (streamNode != null) {
                handleStreamMessage(streamNode.textValue(), json)
            }
        }
    }

    private fun handleStreamMessage(streamName: String, json: JsonNode) {
        if (streamName.endsWith("@aggTrade")) {
            handleAggTradeMessage(json)
        } else if (streamName.endsWith("@depth")) {
            handleDepthMessage(json)
        }
    }

    private fun handleDepthMessage(json: JsonNode) {
        val depthMessage: DepthMessage = Jackson.mapper.convertValue(json)
        log.debug("received depth message: {}", depthMessage)
        depthListener?.let { it(depthMessage) }
    }

    private fun handleAggTradeMessage(json: JsonNode) {
        val aggTradeMessage: AggTradeMessage = Jackson.mapper.convertValue(json)
        log.debug("received aggTrade message: {}", aggTradeMessage)
        aggTradeListener?.let { it(aggTradeMessage) }
    }
}


