package com.randochat.main.socketserver

import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

// one thread. maybe a thread pool for write operations?
class ClientHandler (val directory: ConcurrentHashMap<SelectionKey, String>,
                     val readJobs: ConcurrentLinkedQueue<SelectionKey>): Thread(){


    //one queue for each method?
    fun listen(){
        while (true){
            if (readJobs.peek() != null){
                val conn = readJobs.peek()
                val connect = conn.channel()
                println(connect.isRegistered)
                println(connect.isBlocking)
//                val buffer = ByteBuffer.allocate(1024)
//                var mesLen = conn.channel() //.read(buffer)
//                if (mesLen > -1){
//                    //process message
////                    println(String(Arrays.copyOfRange(buffer.array(), 0, mesLen)))
//                }
                readJobs.remove()


            }
        }

    }

    override fun run() {
        super.run()
        listen()
    }
}