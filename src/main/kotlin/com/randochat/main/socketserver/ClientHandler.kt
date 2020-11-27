package com.randochat.main.socketserver

import org.apache.catalina.Server
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

// one thread. maybe a thread pool for write operations?
class ClientHandler (val directory: ConcurrentHashMap<SocketAddress, SocketChannel>,
                     val readJobs: ConcurrentLinkedQueue<SelectionKey>): Thread(){


    //one queue for each method?
    fun listen(){
        while (true){
            if (readJobs.peek() != null){
                val temp = readJobs.peek().channel() as SocketChannel
                val conn = directory[temp.remoteAddress]!!
                val buffer = ByteBuffer.allocate(1024)
                var mesLen = conn.read(buffer)
                if (mesLen > -1){
                    println("processing message")
                    println(String(Arrays.copyOfRange(buffer.array(), 0, mesLen)))
                }
                readJobs.remove()


            }
        }

    }

    override fun run() {
        super.run()
        listen()
    }
}