package com.randochat.main.socket_server.dataAccsess

import com.randochat.main.socket_server.serverBehavior.Matchmaker
import java.io.IOException
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

//chat room data
//        id: String, uuid
//        members: array of ip addresses? / account Obejcts
//        startTime: timestamp. either Room generation or first transported message
//        nextVote: when the users are able to vote on to continue the chat or reveal the profiles
//        prompt: randomly chosen prompt. a list the choose from should not be replicated
class Room(val id: String, val members: MutableList<User>, var initTime: Long, val startTime: Long, var nextVote: Long, var prompt: String,
           var isHealthy: Boolean, val matchmaker: Matchmaker) {
    companion object{
        fun generateRoom(member: User, matchmaker: Matchmaker): Room {
            val id = UUID.randomUUID()
            val initTime = System.currentTimeMillis()
            val startTime = 0L
            val nextVote = 0L // startTime + 60,000?
            val prompt = "" //rand_choice(prompts)
            val isHealthy = false
            val members = mutableListOf<User>().also { it.add(member) }
            return Room(id.toString(), members, initTime, startTime, nextVote, prompt, isHealthy, matchmaker)
        }
    }
    //todo, timeout on two conenctions but no activity?
    //todo, handle prompts, voting, logic etc
    var isFull = false
    var connectionStatus = HashMap<SocketAddress, Boolean>(1)
//    var isHealthy = true // true if waiting with no disconnects,  or active
    var timeOut = System.currentTimeMillis() + 1000L
    var nextCheck = 0L
    var bothDead = false
    var waitTime = 3000L

    fun add(member: User){
        members.add(member)
        isFull = true
        connectionStatus[members[0].address] = true
        connectionStatus[members[1].address] = true
        isHealthy = true
        timeOut = 0L
    }
    fun getOther(target: SocketAddress): User {
        return if (members[0].address == target) members[1] else members[0]
    }
    fun getSurvivors(): ArrayList<User> {
        val survivors = ArrayList<User>()
        for (conn in connectionStatus.keys){
            if (connectionStatus[conn]!!){
                survivors.add(Directory.getUser(conn))
            }
        }
        return survivors
    }
    fun twoConnections():Boolean{
        //merge this method with isHealthy?
        for (conn in connectionStatus.keys){
            if (!connectionStatus[conn]!!){
                return false
            }
        }
        return true
    }
    fun checkConnection(): Boolean{
        //todo, get disconnected channel. if both disconnected, asjust next check time?
        val key = if (!connectionStatus[members[0].address]!!) members[0] else members[1]

//        val target = Directory.getUser(key)
        nextCheck = System.currentTimeMillis() + 500
        if (bothDead){
            //check both
        }
        try {
            key.socketChannel.write(ByteBuffer.wrap("test".toByteArray()))
        }catch (e: IOException){
            notifyDisconnect(key)
            return false
        }
        notifyReconnect(key)
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
        }else if (!connectionStatus[getOther(user.address).address]!!){
            bothDead = true
        }
        if (timeOut == 0L){
            timeOut = System.currentTimeMillis() + waitTime
        }
        user.socketChannel.close()
        user.socketChannel.socket().close()
        isHealthy = false

    }

    fun notifyReconnect(newUser: User): User? {
        members.forEachIndexed { index, user ->
            if (newUser.userId == user.userId){
                connectionStatus.remove(user.address)
                newUser.pair = user.pair
                Directory.removeUser(user)
                Directory.addUser(newUser)
                members[index] = newUser
                connectionStatus[newUser.address] = true
                if (twoConnections()){
                    isHealthy = true
                    println("saved lobby")
                }
                return newUser
            }
        }
        return null
    }

    fun kill(){
        fun removeAndClose(user: User){
            user.socketChannel.close()
            Directory.removeUser(user)
        }
        //get surviving connections,
        // remove room from directory,
        // remove dead users from directory
        // pass surviing connections to matchmaker
        val survivors = getSurvivors()
        if (survivors.size == 0){
          members.forEach { removeAndClose(it) }
        }else if (survivors.size == 1){
            removeAndClose(getOther(survivors[0].address))
        }
        //todo breaks somewhere around here
        survivors.forEach { matchmaker.addToMatchMaking(it) }
//        Directory.removeRoom()
        println("room cloesd")
        println(System.currentTimeMillis() - initTime)
        Directory.removeRoom(this)
    }
    //when a room is closed mutually not from any connection issue
    fun close():Nothing = TODO()
    //called when room is closed, save the room somewhere?
    fun save(): Nothing = TODO()

}