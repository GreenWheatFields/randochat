package com.randochat.main.socketserver

import java.net.SocketAddress
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap

//normalize data access
//raise exceptions on bad requests?
//directory: map of addresses to users and their permissions
//rooms: roomID to toom Object, room is passed to client so that they can reconnect?
object Directory {
    private val directory = ConcurrentHashMap<SocketAddress, User>()
    private val rooms = HashMap<UUID, Room>()
    private val newEntryTemplate = HashMap<String, Any?>().also {
        it["room"] = null
        it["pair"] = null
        it["socketChannel"] = null
        it["room"] = null
        it["isConnected"] = true

    }
    val testDirectory = HashMap<SocketAddress, User>()


    fun getUser(key: SocketAddress): User{
        //todo, handle bad key
        return directory[key]!!
    }
    fun addUser(user: User){
        directory[user.address] = user
    }
    fun initPair(user1: User, user2: User){
        val room = Room.generateRoom(user1)
        room.add(user2)
        rooms[room.roomID] = room
        //this needs to be sent to both clients ^^
        user1.room = room
        user1.pair = user2
        user2.room = room
        user2.pair = user1
    }
//    fun getBool(key: SocketAddress, field: String): Boolean {
//        return directory[key]!![field] as Boolean
//    }
//    fun getConn(key: SocketAddress): SocketChannel{
//        return directory[key]!!["socketChannel"] as SocketChannel
//    }
//    fun getRoom(key: SocketAddress): Room{
//        return directory[key]!!["room"] as Room
//    }
//    fun isValidRoom(key: SocketAddress): Boolean{
//        return directory[key]!!["room"] != null
//    }
//
//    fun addRoom(key1: SocketAddress, key2: SocketAddress, room: Room){
//        directory[key1]!!["room"] = room
//        directory[key2]!!["room"] = room
//    }
//    fun putNewEntry(key: SocketAddress, channel: SocketChannel){
//        directory[key] = HashMap<String, Any?>(newEntryTemplate).also { it["socketChannel"] = channel }
//    }
//    fun assign(key: SocketAddress, field: String, value: Any?){
//        if (directory[key] != null){
//            //do something
//        }else{
//            directory[key]?.set(field, value)
//        }
//    }
//    fun clearEntry(key: SocketAddress){
//
//    }

}