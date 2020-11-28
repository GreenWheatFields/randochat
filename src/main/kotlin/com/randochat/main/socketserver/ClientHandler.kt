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
        val directory: ConcurrentHashMap<SocketAddress, ConcurrentHashMap<Int, Any>>,
        val readJobs: ConcurrentLinkedQueue<SelectionKey>,
): Thread(){

    val currJobs = Collections.newSetFromMap(ConcurrentHashMap<Int, Boolean>())

    //one queue for each method?
    fun listen(){
        while (true){
            if (readJobs.peek() != null){
                val temp = readJobs.peek().channel() as SocketChannel
                val conn = directory[temp.remoteAddress]?.get(2) as SocketChannel
                var client: SocketChannel? = null
                //check for pair
                if (directory[directory[temp.remoteAddress]!![1]] != null){
                    println("both connected")
                    client = directory[directory[temp.remoteAddress]!![1]]?.get(2) as SocketChannel
                }else{
                    println("client is waiting for connection")
                }
                val buffer = ByteBuffer.allocate(1024)
                conn.read(buffer)
                if (client != null){
                    //todo next, allow client to recieve data
                    client.write(buffer)
                }

//                var mesLen = conn.read(buffer)
//                if (mesLen > -1){
////                    println(String(Arrays.copyOfRange(buffer.array(), 0, mesLen)))
//                }
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