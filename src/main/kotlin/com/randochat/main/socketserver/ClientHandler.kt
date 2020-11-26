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
class ClientHandler (val directory: ConcurrentHashMap<SocketChannel,
        String>, val readJobs: ConcurrentLinkedQueue<SocketChannel>): Thread(){


    //one queue for each method?
    fun listen(){
        while (true){
            if (readJobs.peek() != null){
                val conn = readJobs.peek()
                println(directory[conn])
//                readJobs.

            }
        }

    }

    override fun run() {
        super.run()
        listen()
    }
}