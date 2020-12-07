package com.randochat.main.socketserver

import java.io.IOException
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.util.*
import kotlin.collections.HashMap

//chat room data
//        id: String, uuid
//        members: array of ip addresses? / account Obejcts
//        startTime: timestamp. either Room generation or first transported message
//        nextVote: when the users are able to vote on to continue the chat or reveal the profiles
//        prompt: randomly chosen prompt. a list the choose from should not be replicated
class Room(val id: UUID, val members: MutableList<User>, var initTime: Long, val startTime: Long, var nextVote: Long, var prompt: String,
           var isBothConnected: Boolean) {
    companion object{
        fun generateRoom(member: User): Room {
            val id = UUID.randomUUID()
            val initTime = System.currentTimeMillis()
            val startTime = 0L
            val nextVote = 0L // startTime + 60,000?
            val prompt = "" //rand_choice(prompts)
            val isBothConnected = false
            val members = mutableListOf<User>().also { it.add(member) }
            return Room(id, members, initTime, startTime, nextVote, prompt, isBothConnected)
        }
    }


    var isFull = false
    var connectionStatus = HashMap<SocketAddress, Boolean>()
    var isHealthy = true // true if waiting with no disconnects,  or active
    var timeOut = 0L
    var nextCheck = 0L
    var bothDead = false

    fun add(member: User){
        members.add(member)
        isFull = true
        //todo, no need for an array of booleans no that there is a timeout
        connectionStatus[members[0].address] = true
        connectionStatus[members[1].address] = true
        isHealthy = true
    }
    fun getOther(target: SocketAddress): User {
        return if (members[0].address == target) members[1] else members[0]
    }
//    fun getSurvivors(): Array<SocketAddress> {
//        //this could mess up when a connect is added
//        for (conn in connectionStatus.keys) {
//            if (connectionStatus[conn]!![0]) {
//
//            }
//        }
//    }
    fun twoConnections():Boolean{
        //merge this method with isHealthy?
        for (conn in connectionStatus.keys){
            if (!connectionStatus[conn]!!){
                return false
            }
        }
        return true
    }
    fun notifyDisconnect(user: User) {
        //this might break when a client disconnects before the pair has even joined
        if (!connectionStatus[user.address]!!){
            return
        }
        connectionStatus[user.address] = false
        if (members.size == 1){
            //disconnect before pair has joined
        }else if (!connectionStatus[getOther(user.address)]!!){
            bothDead = true
        }
        if (timeOut == 0L){
            timeOut = System.currentTimeMillis() + 3000L
        }
        isHealthy = false
    }
    fun checkConnection(): Boolean{
        //todo, only check the connection that has been reported as
        //todo, get disconnected channel. if both disconnected, asjust next check time?
        val key = if (!connectionStatus[members[0].address]!!) members[0] else members[1]
//        println(key)
        val target = Directory.getUser(key)
        nextCheck = System.currentTimeMillis() + 500
        if (bothDead){
            //check both
        }
        try {
            target.socketChannel.write(ByteBuffer.wrap("test".toByteArray()))
        }catch (e: IOException){
            notifyDisconnect(target)
            return false
        }
        notifyReconnect(key)
        return true
    }
//    fun kill(survivor: SocketAddress){
////        Directory
//    }
    fun notifyReconnect(target: User){
    //todo, reopened sockets might not be on the same port, at least when theyre on the local network. figure out how to handle and assign reconnections
        if (connectionStatus[target]!!){
            return
        }
    //in here just reassign the users address to the new address and continue one
        connectionStatus[target.address] = true
        isHealthy = twoConnections()
        timeOut = 0L
    }
    //when a room is closed mutually not from any connection issue
    fun close():Nothing = TODO()
    //called when room is closed, save the room somewhere?
    fun save(): Nothing = TODO()



}