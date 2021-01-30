package com.randochat.main.socket_server.serverBehavior

import com.randochat.main.socket_server.dataAccsess.Directory
import com.randochat.main.socket_server.dataAccsess.Room
import com.randochat.main.socket_server.dataAccsess.User
import java.net.SocketAddress
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class Matchmaker (private val clientHandler: ClientHandler): Thread() {
    //handle matchmaking. for now just a queue
    //todo, a handle an authorized user disconneting while waiting for a match
    // probably should be another thread that returns pair objects or something
    //todo, real matchmaking, more than a q
    private val waiting = LinkedList<User>()
    private val waitList = HashSet<SocketAddress>()
    private val testList = ConcurrentLinkedQueue<User>()

    override fun start() {
        super.start()
        matchmake()
    }

    fun addToMatchMaking(user: User): Boolean {
        ///true if further action needed, false if no further action needed
        if (user.pair != null){
            return false
        }
        if (waiting.size == 0) {
            waiting.add(user)
            testList.add(user)
            return true
        } else {
            initPair(user, Directory.getUser(waiting.peek().address))
            waitList.remove(waiting.remove().address)
            waitList.remove(user.address)
            testList.remove()
            //probably should call this from here, just return user?
            clientHandler.sendWelcomeMessage(user)
            return true
        }
    }
    fun matchmake(){
        while (true) {
//            val pair1 = testlis
        }
    }
    fun checkStatus(key: SocketAddress){
//    println(waiting.size)
        //return key.isPaired() or something
    }
    fun initPair(user1: User, user2: User){
        //todo, return pair to two peers and remove them from this serverwldisc
        val room = Room.generateRoom(user1, this)
        room.add(user2)
        Directory.savePair(user1, user2, room)
    }
}