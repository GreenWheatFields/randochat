package com.randochat.main.socketserver.serverBehavior

import com.randochat.main.socketserver.dataAccsess.Directory
import com.randochat.main.socketserver.dataAccsess.Room
import com.randochat.main.socketserver.dataAccsess.User
import com.randochat.main.socketserver.messages.Messages
import com.randochat.main.socketserver.messages.ServerMessages
import java.io.IOException
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
        var talkingTo: User
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
                    talkingTo = Directory.getUser(room.getOther(user.address).address)
                    try {
                        talkingTo.socketChannel.write(ByteBuffer.wrap(message.array()))
                    } catch (e: IOException) {
                        //never caught
                        println("disconnected")
                        room.notifyDisconnect(talkingTo)
                        return
                    }
                }
            }else{
                salvageRoom(room)
            }
        }else{
            // someone with no match or room object
        }
    }
    fun salvageRoom(room: Room){
        if (System.currentTimeMillis() > room.timeOut){
            println("timeout")
            System.exit(5)
        }else if (System.currentTimeMillis() > room.nextCheck){
            room.checkConnection()
        }else{
            return
        }

        }
    fun sendWelcomeMessage(user: User){
        //check null here
        val message = ServerMessages.welcomeMessage(user.room!!)
        user.socketChannel.write(message)
        println(user.pair!!.address)
        println(user.address)
        user.pair?.socketChannel?.write(message)
    }


}


    private fun closeAndCleanUp(): Nothing = TODO()
    //determine remove refrences to dead connections, save room


