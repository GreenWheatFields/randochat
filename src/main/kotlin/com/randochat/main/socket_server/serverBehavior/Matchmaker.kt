package com.randochat.main.socket_server.serverBehavior

import com.randochat.main.socket_server.dataAccsess.User
import com.randochat.main.socket_server.messages.Messages
import java.net.SocketAddress
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

class Matchmaker (private val clientHandler: ClientHandler, val waitList: ConcurrentHashMap<SocketAddress, User>): Thread() {
    //handle matchmaking. for now just a queue
    //todo, a handle an authorized user disconneting while waiting for a match
    // probably should be another thread that returns pair objects or something
    //todo, real matchmaking, more than a q
    private val pairQueue = ConcurrentLinkedQueue<User>()

    override fun start() {
        super.start()
        matchmake()
    }

    fun addToMatchMaking(user: User){
        print("adding")
        if (waitList.contains(user.address)){
            if (user.equals(waitList[user.address])){
                //todo, this wont catch. instead compare every field of the object
            }
        }else{
            print("further adding")
            waitList[user.address] = user
            pairQueue.add(user)
        }
    }
    fun matchmake(){
        while (pairQueue.size > 1) {
            print("matchamking")
            val pair1 = pairQueue.peek()
            val filterGenders = pairQueue.filter { if (it.seeking != 3) it.seeking == pair1.gender else(true)}
            val pair2 = filterGenders.first()
            initPair(pair1, pair2)
        }
    }
    fun checkStatus(key: SocketAddress){
//    println(waiting.size)
        //return key.isPaired() or something
    }
    fun initPair(user1: User, user2: User){
        //for now. ip address of the other user so they can attempt to connect
        //later: prompts, account profiles, vote time etc
        //todo, Pair() object, and don't write to socket from here
        for (i in listOf(user1, user2)){
            i.socketChannel.write(Messages.stringToBuffer("sucesss"))
        }
    }
}