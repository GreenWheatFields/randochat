package com.randochat.main.socketserver

import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

// one thread. maybe a thread pool for write operations?
class ClientHandler(
        val directory: ConcurrentHashMap<SocketAddress, ConcurrentHashMap<String, Any>>,
        val readJobs: ConcurrentLinkedQueue<SelectionKey>
): Thread(){

    val currJobs = Collections.newSetFromMap(ConcurrentHashMap<Int, Boolean>())

    //one queue for each method?
    fun listen(){
        while (true){
            if (readJobs.peek() != null){
                val temp = readJobs.peek().channel() as SocketChannel
                val conn = directory[temp.remoteAddress]?.get("socketChannel") as SocketChannel
                var talkingTo: Any? = null
                //check for pair
                if (directory[directory[temp.remoteAddress]!!["pair"]] != null){
                    println("both connected")
                    talkingTo = directory[directory[temp.remoteAddress]!!["pair"]]?.get("socketChannel")
                }else{
                    println("client is waiting for connection")
                }
                //once everything is established as valid, just relay the message
                val buffer = ByteBuffer.allocate(1024)
                conn.read(buffer)
                if (talkingTo != null){
                    talkingTo as SocketChannel
                    //todo next, allow client to recieve data
                    talkingTo.write(buffer)
                }
                currJobs.remove(readJobs.peek().hashCode())
                readJobs.remove()
            }
        }

    }

    override fun run() {
        super.run()
        listen()
    }
}