package com.randochat.main.socketserver.serverBehavior

import com.randochat.main.socketserver.dataAccsess.Directory
import com.randochat.main.socketserver.dataAccsess.Room
import com.randochat.main.socketserver.dataAccsess.User
import java.net.SocketAddress
import java.util.*

class Matchmaker (private val clientHandler: ClientHandler) {
    //handle matchmaking. for now just a queue
    //todo, a handle an authorized user disconneting while waiting for a match
    // probably should be another thread that returns pair objects or something
    private val waiting = LinkedList<User>()
    private val waitList = HashSet<SocketAddress>()

    fun addToMatchMaking(user: User): Boolean {
        //adds to waitlist if empty of pairs the two.
        if (user.pair != null){
            return true
        }
        if (waiting.size == 0) {
            waiting.add(user)
            return false
        } else {
            initPair(user, Directory.getUser(waiting.peek().address))
            waitList.remove(waiting.remove().address)
            waitList.remove(user.address)
            //probably should call this from here, just return user?
            clientHandler.sendWelcomeMessage(user)
            return true
        }
    }
    fun checkStatus(key: SocketAddress){
//    println(waiting.size)
        //return key.isPaired() or something
    }
    fun initPair(user1: User, user2: User){
        val room = Room.generateRoom(user1, this)
        room.add(user2)
        Directory.savePair(user1, user2, room)
    }
}