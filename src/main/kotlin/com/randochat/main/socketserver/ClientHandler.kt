package com.randochat.main.socketserver

import java.io.IOException
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
            room = directory[temp.remoteAddress]!!["room"] as Room
            if (room.isFull){
                if (room.twoConnections()){
                    var message = ByteBuffer.allocate(1024)
                    try {
                        conn.read(message)
                    }catch (e: IOException){
                        println("disconnect detected")
                        room.connectionStatus[conn.remoteAddress] = false
                        //keep it open for a reconnect?
                        //instead of sending nothing, write the status code of the lobby?
                        return
                    }
                    talkingTo = directory[room.getOther(conn.remoteAddress)]!!["socketChannel"] as SocketChannel

                    try {
                        talkingTo.write(ByteBuffer.wrap(message.array()))
                    }catch (e: IOException){
                        println("send disconnect detected")
                        room.connectionStatus[conn.remoteAddress] = false
//                        talkingTo.close()
                        return
                    }

                }else if (room.lobbyStatus == 0){
                    println("waiting")
                }else if (room.lobbyStatus == 1){
                    println("one disconnect detected")
                    //if one person disconnects and the other is still here, add them back into matchmaking?
                    // set a timeout for the client to reconnect
                }else if (room.lobbyStatus == 2){
                    println("dead lobby")
                    // remove directory entries, close connections!!, save room object, etc
                }
            }
        }else{
            println("here")
            //still waiting for pair or one side disconnected or both sides disconnected?
        }
        currJobs.remove(readJobs.peek().hashCode())
        readJobs.remove()
    }

    override fun run() {
        super.run()
        listen()
    }
}