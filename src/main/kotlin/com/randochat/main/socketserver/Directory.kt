package com.randochat.main.socketserver

import java.net.SocketAddress
import java.nio.channels.SocketChannel
import java.util.concurrent.ConcurrentHashMap

//normalize data access
object Directory {
    private val directory = ConcurrentHashMap<SocketAddress, HashMap<String, Any?>>()
    private val newEntryTemplate = HashMap<String, Any?>().also {
        it["room"] = null
        it["pair"] = null
        it["socketChannel"] = null
        it["room"] = null
        it["isConnected"] = true
    }
    init {

    }

    fun putNewEntry(key: SocketAddress, channel: SocketChannel){
        directory[key] = HashMap<String, Any?>(newEntryTemplate).also { it["socketChannel"] = channel }
    }
}