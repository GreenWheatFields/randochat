package com.randochat.main.socketserver

import java.net.SocketAddress
import java.nio.channels.SocketChannel
import java.util.concurrent.ConcurrentHashMap

//normalize data access
//raise exceptions on bad requests
object Directory {
    private val directory = ConcurrentHashMap<SocketAddress, HashMap<String, Any?>>()
    private val newEntryTemplate = HashMap<String, Any?>().also {
        it["room"] = null
        it["pair"] = null
        it["socketChannel"] = null
        it["room"] = null
        it["isConnected"] = true

    }




    fun getBool(key: SocketAddress, field: String): Boolean {
        return directory[key]!![field] as Boolean
    }
    fun getConn(key: SocketAddress): SocketChannel{
        return directory[key]!!["socketChannel"] as SocketChannel
    }
    fun getRoom(key: SocketAddress): Room{
        return directory[key]!!["room"] as Room
    }
    fun isValidRoom(key: SocketAddress): Boolean{
        return directory[key]!!["room"] != null
    }

    fun addRoom(key1: SocketAddress, key2: SocketAddress, room: Room){
        directory[key1]!!["room"] = room
        directory[key2]!!["room"] = room
    }
    fun putNewEntry(key: SocketAddress, channel: SocketChannel){
        directory[key] = HashMap<String, Any?>(newEntryTemplate).also { it["socketChannel"] = channel }
    }
    fun assign(key: SocketAddress, field: String, value: Any?){
        if (directory[key] != null){
            //do something
        }else{
            directory[key]?.set(field, value)
        }
    }
    fun clearEntry(key: SocketAddress){

    }

}