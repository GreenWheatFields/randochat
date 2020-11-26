package com.randochat.main.socketserver

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.net.SocketException
import java.util.concurrent.atomic.AtomicInteger

// one thread. maybe a thread pool for write operations?
class ClientHandler (): Thread(){
//    private val client: Socket
//    private val output = PrintWriter(client.getOutputStream(), true)
//    private val input = BufferedReader(InputStreamReader(client.getInputStream()))
//    var inputString = input.readLine()
    //cycle through the
    init {

    }

    fun listen(){

        println("disconnect detected")

    }

    override fun run() {
        super.run()
        listen()
    }
}