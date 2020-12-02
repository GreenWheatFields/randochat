package com.randochat.main.socketserver

import java.io.IOException
import java.net.Socket
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap
import kotlin.collections.HashSet

//chat room data
//        id: String, uuid
//        members: array of ip addresses? / account Obejcts
//        startTime: timestamp. either Room generation or first transported message
//        nextVote: when the users are able to vote on to continue the chat or reveal the profiles
//        prompt: randomly chosen prompt. a list the choose from should not be replicated
class Room(val id: UUID, val members: Array<SocketAddress?>, var initTime: Long, val startTime: Long, var nextVote: Long, var prompt: String,
           var isBothConnected: Boolean,) {
    companion object{
        fun generateRoom(member: SocketAddress): Room {
            val id = UUID.randomUUID()
            val initTime = System.currentTimeMillis()
            val startTime = 0L
            val nextVote = 0L // startTime + 60,000?
            val prompt = "" //rand_choice(prompts)
            val isBothConnected = false
            val members = arrayOfNulls<SocketAddress>(2)
            members[0] = member
            return Room(id, members,initTime, startTime, nextVote, prompt, isBothConnected)
        }
    }

    var isFull = false
    var connectionStatus = HashMap<SocketAddress?, Boolean>() //2 elements of array, [0] connected, [1] waiting for reconnect
    var lobbyStatus = 0 //0: waiting with one connection, 1: one disconnected, 2: two disconnected/dead lobby, 3: normal/active
    var isHealthy = true // true if waiting with no disconnects,  or active
    var timeOut = 0L

    fun add(member: SocketAddress){
        members[1] = member
        isFull = true
        //todo, no need for an array of booleans no that there is a timeout
        connectionStatus[members[0]] = true
        connectionStatus[members[1]] = true
        isHealthy = true
        lobbyStatus = 3
    }
    fun getOther(target: SocketAddress): SocketAddress? {
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
//            if (!connectionStatus[conn]!![0]){
//                return false
//            }
        }
        return true
    }
    fun notifyDisconnect(target: SocketAddress): Int {
        //remove entry, adjust lobby status
        //todo, setTimeout to 0 when complete disconnect.
        connectionStatus[target] = false
        val otherConn = getOther(target)
        if (isFull){
            if (connectionStatus[otherConn]!!){
                lobbyStatus = 1
                if (timeOut == 0L){
                    //waiting for reconnection
                    lobbyStatus = 1
                }else{
                    lobbyStatus = 0
                }
            }else{
                lobbyStatus = 2
            }
        }else{
            lobbyStatus = 2
        }
        if (timeOut == 0L){
            timeOut = System.currentTimeMillis() + 3000L
        }
        isHealthy = false
        return lobbyStatus
    }
    fun checkConnection(target: SocketChannel): Boolean{

        try {
            target.write(ByteBuffer.wrap("test".toByteArray()))
        }catch (e: IOException){
            notifyDisconnect(target.remoteAddress)
            return false
        }
        return true
    }
    fun kill(survivor: SocketAddress){
        Directory
    }
    fun notifyReconnect(): Nothing = TODO("isHealthy = True")
    //when a room is closed mutually not from any connection issue
    fun close():Nothing = TODO()
    //called when room is closed, save the room somewhere?
    fun save(): Nothing = TODO()



}