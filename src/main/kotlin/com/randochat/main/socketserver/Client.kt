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
     fun connect(){
         try{
//             sleep(Random().nextInt(3000).toLong())
             conn = SocketChannel.open(InetSocketAddress("localhost", 15620))
             conn.configureBlocking(false)
             val i = Random().nextInt(3000).toString()
             var buff = ByteBuffer.allocate(1024)
             for(temp in 0..100){
                 println(temp)
                 conn.write(ByteBuffer.wrap(i.toByteArray()))

//                 sleep(Random().nextInt(50).toLong())
                 val mesLen = conn.read(buff)
//                 println(String(Arrays.copyOfRange(buff.array(), 0 ,5)))
             }
             //not working
             println("disconnecting")
             conn.close()
         }catch (e: ConnectException){
             println(e.stackTrace)
             sleep(Random().nextInt(100).toLong())
             connect()
         }


     }

    override fun run() {
        super.run()
        connect()
    }
}
