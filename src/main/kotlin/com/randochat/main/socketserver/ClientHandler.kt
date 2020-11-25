package com.randochat.main.socketserver

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.net.SocketException
import java.util.concurrent.atomic.AtomicInteger

class ClientHandler (private val client: Socket, val threadCount: AtomicInteger): Thread(){

    private val output = PrintWriter(client.getOutputStream(), true)
    private val input = BufferedReader(InputStreamReader(client.getInputStream()))
    var inputString = input.readLine()

    fun listen(){
//        println(inputString)
        while (inputString != null){
            println(inputString)
            output.println(System.currentTimeMillis())
            try {
                inputString = input.readLine()
            }catch (e: SocketException) {
                break
            }
    }
        println("disconnect detected")
        println(threadCount.getAndDecrement())

    }

    override fun run() {
        super.run()
        listen()
    }
}