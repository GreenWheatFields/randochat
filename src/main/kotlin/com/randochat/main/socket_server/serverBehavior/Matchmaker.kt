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
        if (waitList.contains(user.address)){
            if (user.equals(waitList[user.address])){
                //todo, this wont catch. instead compare every field of the object
            }else{
                waitList[user.address] = user
                pairQueue.add(user)
            }
        }
//        if (user.pair != null){
//            return false
//        }
//        if (testList.size == 0) {
//            testList.add(user)
//            return true
//        } else {
//            initPair(user, Directory.getUser(testList.peek().address))
//            waitList.remove(testList.remove().address)
//            waitList.remove(user.address)
//            //probably should call this from here, just return user?
//            clientHandler.sendWelcomeMessage(user)
//            return true
//        }
    }
    fun matchmake(){
        while (true) {
            val pair1 = pairQueue.peek()
            val filterGenders = pairQueue.filter { if (it.seeking != 3) it.seeking == pair1.gender else(true)}
            val pair2 = filterGenders.first()
            initPair(pair1, pair2)

            return
        }
    }
    fun checkStatus(key: SocketAddress){
//    println(waiting.size)
        //return key.isPaired() or something
    }
    fun initPair(user1: User, user2: User){
        //for now. ip address of the other user so they can attempt to connect
        //later: prompts, account profiles, vote time etc
        for (i in listOf(user1, user2)){
            i.socketChannel.write(Messages.stringToBuffer("sucesss"))
        }
    }
}