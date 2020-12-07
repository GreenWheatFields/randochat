package com.randochat.main.socketserver

import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.collections.HashSet


/*todo, this server is not a matchmaker. pairs should be created somewhere else.
 *  maybe have a matchmaker server connected on a local network?*/
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


    init {
        Directory
        server.configureBlocking(false)
        server.socket().bind(InetSocketAddress("127.0.0.1", 15620))
        server.register(selector, SelectionKey.OP_ACCEPT)
    }

    fun listen(){
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
//                        acceptConn(key)
                    }
                    if (key.isReadable) {
                        val keyAdd = (key.channel() as SocketChannel).remoteAddress
                        if (authorizer.isSuspect(keyAdd)){
                           if(authorizer.attemptValidate(key.channel() as SocketChannel)){
                               //done with authorizer after this
                               addToMatchMaking(authorizer.authorize(keyAdd))
                           }
                        }else if (waitList.contains(keyAdd)){
                            //catch reconnects attempt here. also people waiting for a pair
                            println("here")
                            checkStatus(keyAdd)
                        }else{
                            clientHandler.read(key.channel())

                        }

                    }

                }
            }
        }
    }
fun addToMatchMaking(user: User){
    if (waiting.size == 0){
            waiting.add(user.address)
        waitList.add(user.address)
        }else{
        println("match found")
        Directory.initPair(user, Directory.getUser(waiting.peek()))
        waitList.remove(waiting.remove())
        waitList.remove(user.address)
        clientHandler.welcome(user)
        }
}
fun checkStatus(key: SocketAddress){
//    println(waiting.size)
}
    override fun run() {
        super.run()
        listen()
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