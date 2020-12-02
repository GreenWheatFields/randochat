package com.randochat.main.socketserver

import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue


// starting with a simple text chat
/*todo, this server is not a matchmaker. pairs should be created somewhere else.
 *  maybe have a matchmaker server connected on a local network?*/
/*
directory<SocketAddress, Any> (key = client ipAddress):
    {
    0 (isConnected): bool, active session
    1 (pair): (approved pair according to matchmaker): ipAddress,
    2 (SocketChannel): SocketChannel object created when connection accepted
    3 (room): room object that contains the status of a room (id, timeAlive, next vote, voteResults, initialPrompt, etc)),
    4 (other)
    }
    concurrentmap doesn't like null, will use int 101 instead
 */
class AcceptConnections: Thread() {

    val selector = Selector.open()
    val server = ServerSocketChannel.open()
    val readJobs = ConcurrentLinkedQueue<SelectionKey>()
    val waiting = LinkedList<SocketAddress>()
    // declare initial capacity?
    val nullCode = 101
    val clientHandler = ClientHandler(directory)


    init {
        Directory
        server.configureBlocking(false)
        server.socket().bind(InetSocketAddress("localhost", 15620))
        server.register(selector, SelectionKey.OP_ACCEPT)
    }

    fun listen(){
        //todo, somewhere, blank lines are being printed
        var accepted = 0
        while (true){
            selector.select()
            val keys = selector.selectedKeys().iterator()
            while (keys.hasNext()){
                val key = keys.next() as SelectionKey
                keys.remove()
                //todo, this gets checked multiple times per key after a key has been assigned to a job
                if (key.isValid) {
                    if (key.isAcceptable) {
                        acceptConn(key)
                    }
                    if (key.isReadable) {
                        clientHandler.read(key.channel())
                    }

                }
            }
        }
    }
    fun acceptConn(key: SelectionKey){
        //use a pool here to handle db calls?
        //this thread should just determine whether a connection is valid.
        val channel = key.channel() as ServerSocketChannel
        val newChan = channel.accept()
        val userKey = newChan.remoteAddress
        newChan.configureBlocking(false)
        newChan.register(selector, SelectionKey.OP_READ, SelectionKey.OP_WRITE)
        //todo, putIfAbsent() here?
        Directory.putNewEntry(userKey, newChan)

        if (waiting.size == 0){
            waiting.add(userKey)
        }else{
            Directory.assign(userKey, "pair", waiting.first)
            if (Directory.getBool(userKey, "isConnected")){
                Room.generateRoom(userKey).also {
                    it.add((directory[waiting.first]!!["socketChannel"] as SocketChannel).remoteAddress)
                    directory[userKey]!!["room"] = it
                    directory[waiting.first]!!["room"] = it
                }

            }else{
                //waiting for client
            }
            waiting.remove()
        }


    }

    override fun run() {
        super.run()
        listen()
    }
}

fun main() {
    println("here")
    val acceptConns = AcceptConnections()
    acceptConns.start()
    repeat(2){
        val client = Client()
        client.start()
    }





}