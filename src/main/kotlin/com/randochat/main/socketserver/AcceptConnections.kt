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
    concurrentmap doesn't like null, will use "null" instead
 */
class AcceptConnections: Thread() {
    fun listen(){
        val selector = Selector.open()
        val server = ServerSocketChannel.open()
        val directory = ConcurrentHashMap<SocketAddress, ConcurrentHashMap<Int, Any>>()
        val readJobs = ConcurrentLinkedQueue<SelectionKey>()
        val waiting = LinkedList<SocketAddress>()
        server.configureBlocking(false)
        server.socket().bind(InetSocketAddress("localhost", 15620))
        server.register(selector, SelectionKey.OP_ACCEPT)
        //maybe host queues in this thread?
        val clientHandler = ClientHandler(directory, readJobs)
        val nullString = "null"
        clientHandler.start()

        var accepted = 0
        while (true){
            selector.select()
            val keys = selector.selectedKeys().iterator()
            while (keys.hasNext()){


                val key = keys.next() as SelectionKey
                var canPass = true
                canPass = !clientHandler.currJobs.contains(key.hashCode())
                keys.remove()
                if (canPass) {
                    if (key.isAcceptable) {
//                        println("accepting")
                        //use a pool here to handle db calls?
                        //this thread should just determine whether a connection is valid.
                        val channel = key.channel() as ServerSocketChannel
                        //might need this towards the end
                        val newChan = channel.accept() ?: break
                        newChan.configureBlocking(false)
                        newChan.register(selector, SelectionKey.OP_READ, SelectionKey.OP_WRITE)
                        directory.put(newChan.remoteAddress, ConcurrentHashMap<Int, Any>())
                        //todo, can this be simplified
                        directory[newChan.remoteAddress]!![0] = true
                        directory[newChan.remoteAddress]!![1] = nullString
                        directory[newChan.remoteAddress]!![2] = newChan
                        directory[newChan.remoteAddress]!![3] = nullString
                        directory[newChan.remoteAddress]!![4] = nullString

                        if (waiting.size == 0){
                            waiting.add(newChan.remoteAddress)
                        }else{
                            directory[newChan.remoteAddress]!![1] = waiting.first
                            if (directory[waiting.first]!![0] == true){
                                //both clients connected
                                println("here")
                            }else{
                                //waiting for client
                            }
                            waiting.remove()
                        }

                    }
                    if (key.isReadable) {
                        clientHandler.currJobs.add(key.hashCode())
                        readJobs.add(key)
                    }

                }
            }
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