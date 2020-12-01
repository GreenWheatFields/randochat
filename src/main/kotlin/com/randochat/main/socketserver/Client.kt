package com.randochat.main.socketserver

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ConnectException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.*

class Client: Thread(){
     lateinit var conn: SocketChannel
     fun connect(sleepTime: Long){
         try{
             sleep(sleepTime)
             println("awake")
             conn = SocketChannel.open(InetSocketAddress("localhost", 15620))
             conn.configureBlocking(false)
             val i = Random().nextInt(3000).toString()
             var buff = ByteBuffer.allocate(1024)
             for(temp in 0..100){
//                 println(Thread.currentThread().name + " is sending " + temp.toString())
                 conn.write(ByteBuffer.wrap(i.toByteArray()))

                 sleep(Random().nextInt(100).toLong())
                 val mesLen = conn.read(buff)

             }
             println(Thread.currentThread().name + "is disconencting")
             conn.socket().close()
         }catch (e: ConnectException){
             println(e.printStackTrace())
             sleep(Random().nextInt(100).toLong())
             connect(Random().nextInt(3000).toLong())
         }


     }

    override fun run() {
        super.run()
        connect(Random().nextInt(3000).toLong())
    }
}
