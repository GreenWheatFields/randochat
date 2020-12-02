package com.randochat.main.socketserver

import java.io.IOException
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectableChannel
import java.nio.channels.SelectionKey
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

// methods to talk to clients
class ClientHandler(
        val directory: ConcurrentHashMap<SocketAddress, ConcurrentHashMap<String, Any>>,
): Thread(){


    fun read(channel: SelectableChannel){
        if (channel !is SocketChannel){
            println("not a socket channe;")
            return
        }
        val conn = directory[channel.remoteAddress]?.get("socketChannel") as SocketChannel //?: do something that stops execution if null
        var talkingTo: SocketChannel
        var room: Room
        if (directory[channel.remoteAddress]!!["room"] !is Int){
            room = directory[channel.remoteAddress]!!["room"] as Room
            if (room.isHealthy){
                if (room.twoConnections()) {
                    var message = ByteBuffer.allocate(1024)
                    try {
                        conn.read(message)
                    } catch (e: IOException) {
                        if (!readLobbyStatus(room.notifyDisconnect(conn.remoteAddress))) {
                            return
                        }
                    }
                    talkingTo = directory[room.getOther(conn.remoteAddress)]!!["socketChannel"] as SocketChannel
                    try {
                        talkingTo.write(ByteBuffer.wrap(message.array()))
                    } catch (e: IOException) {
                        //never caught
                        println("disconnected")
                        if (readLobbyStatus(room.notifyDisconnect(talkingTo.remoteAddress))) {
                            return
                        }
                    }
                }
            }else{
                salvageRoom(room, conn)
            }
        }else{
            // someone with no match or room object
        }
    }
    fun salvageRoom(room: Room, conn: SocketChannel){
        when (room.lobbyStatus){
            0 -> {
                //not getting hit as often as it should?
                println("waiting for reconnect")
            }
            1 -> {
                if (System.currentTimeMillis() > room.timeOut){
                    println("lobby timeout")
                    room.kill(directory, conn.remoteAddress)
                    //make sure there are no refrences. maybe reuse the room?
                    println("roomKilled")
                    System.exit(1)
                }else{
                    //if (currentTime - room.lastConnectionCheck) > 1000?
                    if (room.connectionStatus[conn.remoteAddress]!![1]){
                        // known connection
                        room.isConnected(conn)
                    }else{
                        //todo, send an empty packet to a known disconnect?
                        room.isConnected(conn)
                    }
                }

            }
            2 -> {
                println("dead lobby")
                System.exit(1)
            }
        }
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

}