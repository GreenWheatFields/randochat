package com.randochat.main.socketserver

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.net.SocketException
import java.util.concurrent.atomic.AtomicInteger

// one thread, new requests added to a q?.
class ClientHandler (private val client: Socket): Thread(){

    private val output = PrintWriter(client.getOutputStream(), true)
    private val input = BufferedReader(InputStreamReader(client.getInputStream()))
    var inputString = input.readLine()

    fun listen(){

        println("disconnect detected")

    }

    override fun run() {
        super.run()
        listen()
    }
}