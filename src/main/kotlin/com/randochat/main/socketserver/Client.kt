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
            conn = SocketChannel.open(InetSocketAddress("127.0.0.1", 15620))
            conn.configureBlocking(false)


        }catch (e: ConnectException){
            println(e.printStackTrace())
            sleep(Random().nextInt(100).toLong())
            connect(Random().nextInt(3000).toLong())
        }
        sleep(100)
        introduce()
        println(waitForResponse())
        sleep(100)

        send()
        println(Thread.currentThread().name + "is disconencting")
        conn.socket().close()


    }
    fun waitForResponse(): String {
        var len = -1
        var message = ByteBuffer.allocate(1024)
        var response = ""
        while (len == -1){
            len = conn.read(message)
        }
        println(len)
        for (i in 0 until len){
            response += message[i].toString()
        }
        return response
    }

    fun introduce(){
        conn.write(ByteBuffer.wrap("ACCOUNTID/TOKEN".toByteArray()))
    }
    fun send(){
        val i = Random().nextInt(3000).toString()
        var buff = ByteBuffer.allocate(1024)
        for(temp in 0..100){
//                 println(Thread.currentThread().name + " is sending " + temp.toString())
            conn.write(ByteBuffer.wrap(i.toByteArray()))

            sleep(Random().nextInt(100).toLong())
            val mesLen = conn.read(buff)

        }
    }

    override fun run() {
        super.run()
        connect(Random().nextInt(300).toLong())
    }
}
