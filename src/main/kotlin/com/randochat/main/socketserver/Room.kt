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
class Room(val id: UUID, val members: MutableList<SocketAddress>, var initTime: Long, val startTime: Long, var nextVote: Long, var prompt: String,
           var isBothConnected: Boolean) {
    companion object{
        fun generateRoom(member: SocketAddress): Room {
            val id = UUID.randomUUID()
            val initTime = System.currentTimeMillis()
            val startTime = 0L
            val nextVote = 0L // startTime + 60,000?
            val prompt = "" //rand_choice(prompts)
            val isBothConnected = false
            val members = mutableListOf<SocketAddress>()
            members.add(member)
            return Room(id, members, initTime, startTime, nextVote, prompt, isBothConnected)
        }
    }


    var isFull = false
    var connectionStatus = HashMap<SocketAddress, Boolean>()
    var isHealthy = true // true if waiting with no disconnects,  or active
    var timeOut = 0L
    var nextCheck = 0L
    var bothDead = false

    fun add(member: SocketAddress){
        members.add(member)
        isFull = true
        //todo, no need for an array of booleans no that there is a timeout
        connectionStatus[members[0]] = true
        connectionStatus[members[1]] = true
        isHealthy = true
    }
    fun getOther(target: SocketAddress): SocketAddress {
        return if (members[0] == target) members[1] else members[0]
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
    fun notifyDisconnect(target: SocketAddress) {
        //this might break when a client disconnects before the pair has even joined
        if (!connectionStatus[target]!!){
            return
        }
        connectionStatus[target] = false
        if (members.size == 1){
            //disconnect before pair has joined
        }else if (!connectionStatus[getOther(target)]!!){
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
        val key = if (!connectionStatus[members[0]]!!) members[0] else members[1]
        println(key)
        val target = Directory.getConn(key)
        nextCheck = System.currentTimeMillis() + 500
        if (bothDead){
            //check both
        }
        try {
            target.write(ByteBuffer.wrap("test".toByteArray()))
        }catch (e: IOException){
            notifyDisconnect(target.remoteAddress)
            return false
        }
//        notifyReconnect()
        return true
    }
    fun kill(survivor: SocketAddress){
//        Directory
    }
    fun notifyReconnect(target: SocketAddress){
        if (connectionStatus[target]!!){
            return
        }
        connectionStatus[target] = true
        isHealthy = twoConnections()
        timeOut = 0L
    }
    //when a room is closed mutually not from any connection issue
    fun close():Nothing = TODO()
    //called when room is closed, save the room somewhere?
    fun save(): Nothing = TODO()



}