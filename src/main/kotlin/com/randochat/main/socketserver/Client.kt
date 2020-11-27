package com.randochat.main.socketserver

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.*

class Client: Thread(){
     fun connect(){
         val conn = SocketChannel.open(InetSocketAddress("localhost", 15620))
         val i = "test"
         for(temp in 0..1){
             conn.write(ByteBuffer.wrap("temp.toString()".toByteArray()))
             sleep(1000)
         }




//         val conn = Socket("localhost", 15620)
//         val output = PrintWriter(conn.getOutputStream(), true)
//         val input = BufferedReader(InputStreamReader(conn.getInputStream()))
//         var counter = 0
//         output.println("inital packet")
//         var inputString: String = input.readLine()
//
//         while (true){
//
////             println(inputString)
//             sleep(1000)
//             val temp = UUID.randomUUID()
//             print(temp)
//             println(Thread.currentThread().name)
//             output.println(temp)
//             counter++
//             if (counter > 10) conn.close()
//             try {
//                 inputString = input.readLine()
//             }catch (e: SocketException){
//                 break
//             }
//         }
//         println("break")
     }

    override fun run() {
        super.run()
        connect()
    }
}
