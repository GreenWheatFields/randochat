package com.randochat.main.socketserver.tests

import com.randochat.main.socketserver.messages.Messages
import java.net.ConnectException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.*
import javax.json.JsonObject

class Client: Thread(){
    lateinit var conn: SocketChannel
    fun connect(sleepTime: Long, message: String): String{
        try{
            sleep(sleepTime)
//            println("awake")
            conn = SocketChannel.open(InetSocketAddress("127.0.0.1", 15620))
            conn.configureBlocking(false)


        }catch (e: ConnectException){
            println(e.printStackTrace())
            sleep(Random().nextInt(100).toLong())
            connect(Random().nextInt(3000).toLong(), message)
        }
        sleep(100)
        introduce(message)
        val response = waitForResponse()
        val json = Messages.messageFromBuffer(ByteBuffer.wrap(response.toByteArray()))
        val roomId = (json["status"] as JsonObject).get("roomID")
        sleep(100)
        send()
//        println(Thread.currentThread().name + "is disconencting")
        conn.socket().close()
        return roomId.toString()





    }
    fun waitForResponse(): String {
        var len = -1
        var message = ByteBuffer.allocate(1024)
        var response = ""
        while (len <= 0){
            len = conn.read(message)
        }
        for (i in 0 until len){
            response += message[i].toChar()
        }
        return response
    }

    fun introduce(message: String){
        conn.write(ByteBuffer.wrap(message.toByteArray()))
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
        println(connect(Random().nextInt(300).toLong(), "HELLO"))
//        connect(Random().nextInt(300).toLong(), "RECONNECT")

    }
}
