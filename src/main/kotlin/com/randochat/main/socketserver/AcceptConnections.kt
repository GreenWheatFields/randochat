package com.randochat.main.socketserver

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.SocketException
import java.util.concurrent.atomic.AtomicInteger

//one thread per client for now.
// the directory of connections should be an object that is never replicated and modified by one class.
// starting with a simple text chat
class AcceptConnections: Thread() {
    fun listen(){
        val threadLimit = 1000
        val threads = AtomicInteger()
        val server = ServerSocket(15620)
        while (threads.get() < threadLimit){
            val clientThread = ClientHandler(server.accept(), threads)
            clientThread.start()
            threads.getAndIncrement()
            println(threads.get())
        }

//        val output = PrintWriter(client.getOutputStream(), true)
//        val input = BufferedReader(InputStreamReader(client.getInputStream()))
//        var inputString = input.readLine()
//        while (inputString != null){
////            println(inputString)
//            output.println(System.currentTimeMillis())
//            try {
//                inputString = input.readLine()
//            }catch (e: SocketException) {
//                break
//            }
//        }
//        println("disconnect detected")
    }

    override fun run() {
        super.run()
        listen()
    }
}

fun main() {
    val acceptConns = AcceptConnections()
    acceptConns.start()
    val client1 = Client()
    val client2 = Client()
    client1.start()
    client2.start()
    acceptConns.join()
    client1.join()
    client2.join()

}