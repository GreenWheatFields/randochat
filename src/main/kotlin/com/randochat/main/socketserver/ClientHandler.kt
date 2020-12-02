package com.randochat.main.socketserver

import java.io.IOException
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectableChannel
import java.nio.channels.SocketChannel

// methods to talk to clients
class ClientHandler(): Thread(){


    fun read(channel: SelectableChannel){
        if (channel !is SocketChannel){
            println("not a socket channe;")
            return
        }
        val conn = Directory.getConn(channel.remoteAddress) //?: do something that stops execution if null
        var talkingTo: SocketChannel
        var room: Room
        if (Directory.isValidRoom(conn.remoteAddress)){
            room = Directory.getRoom(conn.remoteAddress)
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
                    talkingTo = Directory.getConn(room.getOther(conn.remoteAddress) as SocketAddress)
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
                println(System.currentTimeMillis() > room.timeOut)
                //todo, room.lobby status never equals 1
            }
            1 -> {
                if (System.currentTimeMillis() > room.timeOut){
                    println("lobby timeout")
//                    room.kill()
                    //reuse the room?
                    println("roomKilled")
                    System.exit(1)
                }else{
//                    if (room.connectionStatus[conn.remoteAddress]!![1]){
//                        // known connection
//                        room.checkConnection(conn)
//                    }else{
//                        //todo, send an empty packet to a known disconnect?
//                        room.checkConnection(conn)
//                    }
                    room.checkConnection(conn)
                }

            }
            2 -> {
                //two disconnectes
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