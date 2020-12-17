package com.randochat.main.socketserver.serverBehavior

import com.randochat.main.socketserver.tests.Client
import com.randochat.main.socketserver.dataAccsess.Directory
import com.randochat.main.socketserver.dataAccsess.User
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.collections.HashSet


class DirectConnections: Thread() {

    val selector = Selector.open()
    val server = ServerSocketChannel.open()
    val readJobs = ConcurrentLinkedQueue<SelectionKey>()
    val waiting = LinkedList<SocketAddress>()
    val waitList = HashSet<SocketAddress>()
    // declare initial capacity?
    val nullCode = 101
    val clientHandler = ClientHandler()
    val authorizer = Authorizer(selector)
    val directory = Directory
    private val matchmaker = Matchmaker(clientHandler)


    init {
//        Directory
        server.configureBlocking(false)
        server.socket().bind(InetSocketAddress("127.0.0.1", 15620))
        server.register(selector, SelectionKey.OP_ACCEPT)
    }
    fun routeConnections(){
        var accepted = 0
        while (true){
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
                           }else{
                               println("unsuccesful validation")
                               authorizer.killSuspect(key.channel() as SocketChannel)
                           }
                        }else if (waitList.contains(keyAdd)){
                            //catch reconnects attempt here. also people waiting for a pair
                            matchmaker.checkStatus(keyAdd)
                        }else{
                            clientHandler.read(key.channel())

                        }

                    }

                }
            }
        }
    }

    override fun run() {
        super.run()
        routeConnections()
    }
}


fun main() {
    val acceptConns = DirectConnections()
    acceptConns.start()
    repeat(2){
        val client = Client()
        client.start()
    }





}