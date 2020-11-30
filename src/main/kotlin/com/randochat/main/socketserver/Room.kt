package com.randochat.main.socketserver

import java.net.Socket
import java.net.SocketAddress
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap

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
    var connectionStatus = HashMap<SocketAddress?, Boolean>(2)
    var lobbyStatus = 0 //0: waiting with one connection, 1: one disconnected, 2: two disconnected/dead lobby


    fun add(member: SocketAddress){
        members[1] = member
        isFull = true
        connectionStatus[members[0]] = true
        connectionStatus[members[1]] = true
    }
    fun getOther(target: SocketAddress): SocketAddress?{
        return if (members[0] == target) members[1] else members[0]
    }
    fun twoConnections():Boolean{ //return true if two connections,
        if (isFull){
            if (connectionStatus[members[0]]!!){
                if (connectionStatus[members[1]]!!){
                    return true
                }
                return false
            }
        }
        return false
    }
    fun notifyDisconnect(target: SocketAddress){
        //remove entry, adjust lobby status
    }

    //called when room is closed, save the room somewhere?
    fun save(): Nothing = TODO()



}