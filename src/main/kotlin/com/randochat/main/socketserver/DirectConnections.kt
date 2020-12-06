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


/*todo, this server is not a matchmaker. pairs should be created somewhere else.
 *  maybe have a matchmaker server connected on a local network?*/
class DirectConnections: Thread() {

    val selector = Selector.open()
    val server = ServerSocketChannel.open()
    val readJobs = ConcurrentLinkedQueue<SelectionKey>()
    val waiting = LinkedList<SocketAddress>()
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

                        if (authorizer.isSuspect((key.channel() as SocketChannel).remoteAddress)){
                           if(authorizer.attemptValidate(key.channel() as SocketChannel)){
                               //done with authorizer after this
                               //todo, basic matchmaker class. for now itll just be within this class
                               addToMatchMaking(authorizer.authorize((key.channel() as SocketChannel).remoteAddress))


                           }
                        }else{
                            clientHandler.read(key.channel())
                        }
                        //if key.remoteAddress. isAutorized
//                        clientHandler.read(key.channel())
                    }

                }
            }
        }
    }
fun addToMatchMaking(user: User){
//    if (waiting.size == 0){
//            waiting.add(user.id)
//        }else{
//            Directory.assign(userKey, "pair", waiting.first)
//            if (Directory.getBool(userKey, "isConnected")){
//                Room.generateRoom(userKey).also {
//                    it.add(waiting.first)
//                    Directory.addRoom(userKey, waiting.first, it)
//                }
//
//            }else{
//                //waiting for client
//            }
//            waiting.remove()
//        }
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