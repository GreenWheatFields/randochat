package com.randochat.main.socket_server

import com.randochat.main.socket_server.messages.Messages
import java.net.ConnectException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.*
import javax.json.JsonObject

open class Client (val port: Int): Thread(){
    lateinit var conn: SocketChannel
    fun connect(sleepTime: Long, recursive: Boolean){
        try{
            sleep(sleepTime)
//            println("awake")
            conn = SocketChannel.open(InetSocketAddress("127.0.0.1", port))
            conn.configureBlocking(false)


        }catch (e: ConnectException){
            println(e.printStackTrace())
            if (recursive){
                sleep(Random().nextInt(100).toLong())
                connect(Random().nextInt(3000).toLong(),recursive)
            }

        }
//        sleep(100)
//        introduce(message)
//        val response = waitForResponse()
//        val json = Messages.messageFromBuffer(ByteBuffer.wrap(response.toByteArray()))
//        val roomId = (json["status"] as JsonObject).get("roomID")
//        sleep(100)
//        send()
////        println(Thread.currentThread().name + "is disconencting")
//        conn.socket().close()





    }
    fun waitForResponse(): JsonObject {
        var len = -1
        var message = ByteBuffer.allocate(1024)
        var response = ""
        while (len <= 0){
            len = conn.read(message)
        }
        for (i in 0 until len){
            response += message[i].toChar()
        }
        return Messages.messageFromJsonString(response) //redundant
    }

    fun introduce(message: String){
        conn.write(ByteBuffer.wrap(message.toByteArray()))
    }
    fun send(until: Int, sleep: Boolean = true){
        val i = Random().nextInt(3000).toString()
        var buff = ByteBuffer.allocate(1024)
        for(temp in 0..until){
//                 println(Thread.currentThread().name + " is sending " + temp.toString())
            conn.write(ByteBuffer.wrap(i.toByteArray()))
            if (sleep){

            }
            if (sleep){
                sleep(Random().nextInt(100).toLong())
            }
            val mesLen = conn.read(buff)

        }
    }
    fun closeConnection(){
        conn.socket().close()

    }

    override fun run() {
        super.run()
        println(connect(Random().nextInt(300).toLong(),true))
//        connect(Random().nextInt(300).toLong(), "RECONNECT")

    }
}
