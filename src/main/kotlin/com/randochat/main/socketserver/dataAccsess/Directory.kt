package com.randochat.main.socketserver.dataAccsess

import java.net.SocketAddress
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap

//normalize data access
//raise exceptions on bad requests?
//directory: map of addresses to users and their permissions
//rooms: roomID to toom Object, room is passed to client so that they can reconnect?
object Directory {
    val directory = ConcurrentHashMap<SocketAddress, User>()
    private val rooms = HashMap<String, Room>()
    private val newEntryTemplate = HashMap<String, Any?>().also {
        it["room"] = null
        it["pair"] = null
        it["socketChannel"] = null
        it["room"] = null
        it["isConnected"] = true

    }
    val testDirectory = HashMap<SocketAddress, User>()


    fun getUser(key: SocketAddress): User {
        //todo, getting user by socketAddress is stupid
        //todo, handle bad key
        return directory[key]!!
    }
    fun addUser(user: User){
        directory[user.address] = user
    }
    fun removeUser(user: User){
        directory.remove(user.address)
    }
    fun savePair(user1: User, user2: User, room: Room){

        rooms["\"${room.id}\""] = room
        //wrap with quotes because JsonObject.get(roomID) is wrapped in quotes
        user1.room = room
        user1.pair = user2
        user2.room = room
        user2.pair = user1
    }
    fun validRoom(id: String): Boolean{
        return rooms.contains(id)
    }
    fun getRoom(id: String): Room{
        return rooms[id]!!
    }
    fun removeRoom(room: Room){
        directory.remove("\"${room.id}\"")
    }


}