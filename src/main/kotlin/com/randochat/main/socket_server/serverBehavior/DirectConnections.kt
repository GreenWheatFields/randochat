package com.randochat.main.socket_server.serverBehavior

import com.randochat.main.socket_server.dataAccsess.Directory
import java.net.BindException
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.collections.HashSet


class DirectConnections(val port: Int): Thread() {

    var selector = Selector.open()
    val server = ServerSocketChannel.open()
    val readJobs = ConcurrentLinkedQueue<SelectionKey>()
    val waiting = LinkedList<SocketAddress>()
    val waitList = HashSet<SocketAddress>()
    // declare initial capacity?
    val nullCode = 101
    val clientHandler = ClientHandler()
    var authorizer = Authorizer(selector)
    var directory = Directory
    var flag = true
    private val matchmaker = Matchmaker(clientHandler)


    init {
        server.configureBlocking(false)
        server.socket().bind(InetSocketAddress("127.0.0.1", port))
        server.register(selector, SelectionKey.OP_ACCEPT)
        matchmaker.start()
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
                               if (!matchmaker.addToMatchMaking(authorizer.authorize(keyAdd))){
//                                   clientHandler.read(key.channel())
                               }
                           }else{
                               authorizer.killSuspect(key.channel() as SocketChannel)
                           }
                        }else if (waitList.contains(keyAdd)){
                            //catch reconnects attempt here. also people waiting for a pair
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

    override fun run() {
        super.run()
        routeConnections()
    }
}


fun main() {
    val acceptConns = DirectConnections(15620)
    acceptConns.start()
}