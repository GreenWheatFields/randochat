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
        if (testList.size == 0) {
            testList.add(user)
            return true
        } else {
            initPair(user, Directory.getUser(testList.peek().address))
            waitList.remove(testList.remove().address)
            waitList.remove(user.address)
            //probably should call this from here, just return user?
            clientHandler.sendWelcomeMessage(user)
            return true
        }
    }
    fun matchmake(){
        while (true) {
            val pair1 = testList.peek()
            val filterGenders = testList.filter { if (it.seeking != 3) it.seeking == pair1.gender else(true)}
            val pair2 = filterGenders.first()
            println(pair1.address)
            println(pair2.address)
            return
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