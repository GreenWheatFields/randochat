package com.randochat.main.socketserver

import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ConcurrentHashMap

// one thread. maybe a thread pool for write operations?
class ClientHandler (val selector: Selector, val directory: ConcurrentHashMap<Any, Any>): Thread(){

    init {
    }


    fun listen(){
//        while (true){
//            selector.select()
//            val keys = selector.selectedKeys().iterator()
//
//            while (keys.hasNext()) {
//                val key = keys.next() as SelectionKey
//                keys.remove()
//
////                if (key.isConnectable){
////                    //handle dc
////                    println("connectable")
////                }
//                if (key.isReadable) {
//                    key.hashCode()
//                    val channel = key.channel() as SocketChannel
//                    channel.configureBlocking(false)
//
//                    val buffer = ByteBuffer.allocate(1024)
//                    var mesLen = -1
//                    mesLen = channel.read(buffer)
//                    if (mesLen > -1) {
//                        println(String(Arrays.copyOfRange(buffer.array(), 0, mesLen)))
//                    }
//
//                    //read message, check isReadble in here?
//                } else if (false) {
//                    ;
//                }
//            }
//        }

    }

    override fun run() {
        super.run()
        listen()
    }
}