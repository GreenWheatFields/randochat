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
            if (room.isHealthy){
                if (room.twoConnections()) {
                    var message = ByteBuffer.allocate(1024)
                    try {
                        conn.read(message)
                    } catch (e: IOException) {
                        println("disconnect detected")
                        if (readLobbyStatus(room.notifyDisconnect(conn.remoteAddress))) {
                            // ok end lobby
                            return

                        } else {
                            //continue
                            println("continuing")
                        }
                        //keep it open for a reconnect?

                    }
                    talkingTo = directory[room.getOther(conn.remoteAddress)]!!["socketChannel"] as SocketChannel
                    try {
                        talkingTo.write(ByteBuffer.wrap(message.array()))
                    } catch (e: IOException) {
                        println("disconnected")
                        if (readLobbyStatus(room.notifyDisconnect(talkingTo.remoteAddress))) {
                            return
                        } else {
                            println("going on")
                        }
                    }
                }
            }else{
                when (room.lobbyStatus){
                    0 -> {
                        //never getting called
                     println("waiting")
                    }
                    1 -> {
                        //wait for timeout, then kill. 3 seconds for now
                        if (System.currentTimeMillis() - room.timeOut > 3000){
                            // kill room, if any connections send them back to queue, if not save room statistics.
                            System.exit(5)
                        }

                    }
                    2 -> {
                        println("dead lobby")
                    }
                }


                //still waiting for pair or one side disconnected or both sides disconnected?
            }
        }else{
            // someone with no match or room object
        }
        currJobs.remove(readJobs.peek().hashCode())
        readJobs.remove()
    }
    private fun closeAndCleanUp(): Nothing = TODO()
    //determine remove refrences to dead connections, save room

    private fun readLobbyStatus(code: Int): Boolean{
        when (code){
            1 -> return false
            2 -> return true

        }
        return false
    }

    override fun run() {
        super.run()
        listen()
    }
}