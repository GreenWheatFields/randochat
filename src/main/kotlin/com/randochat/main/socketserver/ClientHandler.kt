package com.randochat.main.socketserver

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
                read()
            }

        }
    }
    fun read(){
        val temp = readJobs.peek().channel() as SocketChannel
        val conn = directory[temp.remoteAddress]?.get("socketChannel") as SocketChannel //?: do something that stops execution if null
        var talkingTo: SocketChannel
        var room: Room
        if (directory[temp.remoteAddress]!!["room"] !is Int){
//            println("here")
            room = directory[temp.remoteAddress]!!["room"] as Room
            if (room.isFull){
//                println("full room")
//                println(room.twoConnections(directory))
//                if (room.isBothConnected)
                room.twoConnections(directory)
//                talkingTo = directory[room.getOther(conn.remoteAddress)]!!["socketChannel"] as SocketChannel

            }
        }else{
//            println("unfull room")
            //still waiting for pair. client sending messages earlier than allowed
        }
////            println(directory[talkingTo.remoteAddress]?.get("isConnected"))
//        }else{
////            println("client is waiting for connection")
//        }
        //once everything is established as valid, simply relay the message
//        val buffer = ByteBuffer.allocate(1024)
//        conn.read(buffer)
//        if (talkingTo != null){
//            talkingTo as SocketChannel
//            //todo next, allow client to recieve data
//            talkingTo.write(buffer)
//        }
        currJobs.remove(readJobs.peek().hashCode())
        readJobs.remove()
    }

    override fun run() {
        super.run()
        listen()
    }
}