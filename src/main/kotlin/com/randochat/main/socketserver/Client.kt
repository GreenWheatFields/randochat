package com.randochat.main.socketserver

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class Client: Thread(){
     fun connect(){
         val conn = Socket("localhost", 15620)
         val output = PrintWriter(conn.getOutputStream(), true)
         val input = BufferedReader(InputStreamReader(conn.getInputStream()))
         var counter = 0
         while (conn.isConnected){
             output.println(counter)
             counter++
             println(input.readLine())
             sleep(1000)
             if (counter > 10) conn.close()
         }
     }

    override fun run() {
        super.run()
        connect()
    }
}
