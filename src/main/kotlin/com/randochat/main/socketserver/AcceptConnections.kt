package com.randochat.main.socketserver

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.*
import java.nio.ByteBuffer
import java.nio.channels.*
import java.nio.channels.ServerSocketChannel
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.reflect.typeOf


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
    val directory = ConcurrentHashMap<SocketAddress, ConcurrentHashMap<String, Any>>()
    val nullCode = 101
    val clientHandler = ClientHandler(directory, readJobs)
    private val roomCreator: Room = Room()


    init {
        server.configureBlocking(false)
        server.socket().bind(InetSocketAddress("localhost", 15620))
        server.register(selector, SelectionKey.OP_ACCEPT)
    }

    fun listen(){
        clientHandler.start()
        var accepted = 0
        while (true){
            selector.select()
            val keys = selector.selectedKeys().iterator()
            while (keys.hasNext()){
                val key = keys.next() as SelectionKey
                keys.remove()
                //todo, this gets checked multiple times per key after a key has been assigned to a job
                if (!clientHandler.currJobs.contains(key.hashCode())) {
                    if (key.isAcceptable) {
                        acceptConn(key)
                    }
                    if (key.isReadable) {
                        clientHandler.currJobs.add(key.hashCode())
                        readJobs.add(key)
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
        directory.put(userKey, ConcurrentHashMap<String, Any>()).also{
            directory[userKey]!!["isConnected"] = true
            directory[userKey]!!["pair"] = nullCode
            directory[userKey]!!["socketChannel"] = newChan
            directory[userKey]!!["room"] = nullCode
            directory[userKey]!!["other"] = nullCode
        }

        if (waiting.size == 0){
            waiting.add(userKey)
        }else{
            directory[userKey]!!["pair"] = waiting.first
            if (directory[waiting.first]!!["isConnected"] == true){
                //both clients connected
                //todo, generate Room here?
                directory
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
    val acceptConns = AcceptConnections()
    acceptConns.start()
    repeat(2){
        val client = Client()
        client.start()
    }





}