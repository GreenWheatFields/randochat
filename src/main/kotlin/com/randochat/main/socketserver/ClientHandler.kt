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
        val user = Directory.getUser(channel.remoteAddress) //?: do something that stops execution if null
        var talkingTo: SocketChannel
        var room: Room
        if (user.room != null){
            room = user.room as Room
            if (room.isHealthy){
                if (room.twoConnections()) {
                    var message = ByteBuffer.allocate(1024)
                    try {
                        user.socketChannel.read(message)
                    } catch (e: IOException) {
                        room.notifyDisconnect(user)
                        return
                    }
                    talkingTo = Directory.getConn(room.getOther(conn.remoteAddress) as SocketAddress)
                    try {
                        talkingTo.write(ByteBuffer.wrap(message.array()))
                    } catch (e: IOException) {
                        //never caught
                        println("disconnected")
                        room.notifyDisconnect(talkingTo.remoteAddress)
                        return
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
        if (System.currentTimeMillis() > room.timeOut){
            println("timeout")
            System.exit(5)
        }else if (System.currentTimeMillis() > room.nextCheck){
            room.checkConnection()
        }else{

        }

        }

    }

    private fun closeAndCleanUp(): Nothing = TODO()
    //determine remove refrences to dead connections, save room


