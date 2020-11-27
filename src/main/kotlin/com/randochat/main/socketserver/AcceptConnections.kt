package com.randochat.main.socketserver

import org.aspectj.apache.bcel.generic.InstructionConstants
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
import kotlin.collections.HashSet
import kotlin.reflect.typeOf


// the directory of connections should be an object that is never replicated and modified by one class.
// starting with a simple text chat
class AcceptConnections: Thread() {
    fun listen(){
        val selector = Selector.open()
        val server = ServerSocketChannel.open()
        val directory = ConcurrentHashMap<SocketAddress, SocketChannel>()
        val readJobs = ConcurrentLinkedQueue<SelectionKey>()
        server.configureBlocking(false)
        server.socket().bind(InetSocketAddress("localhost", 15620))
        server.register(selector, SelectionKey.OP_ACCEPT)
        //maybe host queues in this thread?
        val clientHandler = ClientHandler(directory, readJobs)
        clientHandler.start()
        while (true){
            selector.select()
            val keys = selector.selectedKeys().iterator()
            while (keys.hasNext()){
                val key = keys.next() as SelectionKey
                keys.remove()
                if (key.isAcceptable){
                    println("accepting")
                    //use a pool here to handle db calls
                    //this thread should just determine whether a connection is valid.
                    val channel = key.channel() as ServerSocketChannel
                    val newChan = channel.accept()
                    println(newChan.remoteAddress)
                    newChan.configureBlocking(false)
                    newChan.register(selector, SelectionKey.OP_READ, SelectionKey.OP_WRITE)
                    //hashset of permissions
                    println(newChan.hashCode())
                    directory.put(newChan.remoteAddress, newChan)

                }
                if (key.isReadable){
                    readJobs.add(key)
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
    repeat(1){
        val client = Client()
        client.start()
    }





}