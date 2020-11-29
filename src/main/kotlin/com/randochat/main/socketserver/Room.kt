package com.randochat.main.socketserver

import java.net.Socket
import java.net.SocketAddress
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ConcurrentHashMap

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

    fun add(member: SocketAddress){
        members[0] = member
        isFull = true
    }
    fun getOther(target: SocketAddress): SocketAddress?{
        return if (members[0] == target) members[1] else members[0]
    }
    fun twoConnections(directory: ConcurrentHashMap<SocketAddress, ConcurrentHashMap<String, Any>>):Boolean{
        println()
        if (members[0] is SocketAddress && members[1] is SocketAddress){
            println("called")
            if ((directory[members[0]]!!["socketChannel"] as SocketChannel).isConnected && (directory[members[1]]!!["socketChannel"] as SocketChannel).isConnected)
                return true
        }
        return false
    }
    //called when room is closed, save the room somewhere?
    fun save(): Nothing = TODO()



}