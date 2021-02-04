package com.randochat.main.socket_server.serverBehavior

import com.randochat.main.socket_server.dataAccsess.Directory
import com.randochat.main.socket_server.dataAccsess.User
import java.net.BindException
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.collections.HashSet


class DirectConnections(val port: Int): Thread() {

    var selector = Selector.open()
    val server = ServerSocketChannel.open()
    val readJobs = ConcurrentLinkedQueue<SelectionKey>()
    val waiting = LinkedList<SocketAddress>()
    val waitList = ConcurrentHashMap<SocketAddress, User>()
    // declare initial capacity?
    val nullCode = 101
    val clientHandler = ClientHandler()
    var authorizer = Authorizer(selector)
    var directory = Directory
    var flag = true
    val matchmaker = Matchmaker(clientHandler, waitList)



    init {
        server.configureBlocking(false)
        server.socket().bind(InetSocketAddress("127.0.0.1", port))
        server.register(selector, SelectionKey.OP_ACCEPT)

    }
    fun routeConnections(){
        //todo, keep track of some data for analytics like current connects, etc, etc
        var accepted = 0
        while (flag){
            selector.select()
            val keys = selector.selectedKeys().iterator()
            while (keys.hasNext()){
                val key = keys.next() as SelectionKey
                keys.remove()
                if (key.isValid) {
                    if (key.isAcceptable) {
                        authorizer.investigateConn(key)
                    }
                    if (key.isReadable) {
                        val keyAdd = (key.channel() as SocketChannel).remoteAddress
                        if (authorizer.isSuspect(keyAdd)){
                           if(authorizer.attemptValidate(key.channel() as SocketChannel)){
                               matchmaker.addToMatchMaking(authorizer.authorize(keyAdd))
//                               if (!matchmaker.addToMatchMaking(authorizer.authorize(keyAdd))){
////                                   clientHandler.read(key.channel())
//                               }
                           }else{
                               authorizer.killSuspect(key.channel() as SocketChannel)
                           }
                        }else if (waitList.containsKey(keyAdd)){
                            //reconnect while still in queue
                            matchmaker.checkStatus(keyAdd)
                        }else{
                            //clientHandler.read(key.channel())

                        }

                    }

                }
                if (authorizer.lastSweep < System.currentTimeMillis()){
                    //probably should be checked on another thread
                    authorizer.sweep()

                }
            }
        }
    }
    fun bind(): Boolean{
        try {
            server.socket().bind(InetSocketAddress("127.0.0.1", port))
        }catch (e: BindException){
            return false
        }
        return true
    }
    fun close(){
        matchmaker.flag = false
        matchmaker.interrupt()

    }

    override fun run() {
        println(currentThread().id)
        matchmaker.start()
        routeConnections()

    }
}


fun main() {
    val acceptConns = DirectConnections(15620).start()

}