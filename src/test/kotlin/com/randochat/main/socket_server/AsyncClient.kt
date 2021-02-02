package com.randochat.main.socket_server

import com.randochat.main.socket_server.messages.ClientMessages
import com.randochat.main.socket_server.messages.Messages
import java.nio.ByteBuffer

//what an actual client will look like. listening and talking on two threaders
//todo, method that listens writes and occasionally dsconnects/reconnects/ doesnt authorize/ sits idle/ etcetc
class AsyncClient(val client: Client): Thread() {
    var flag = true
    var length = -1
    var index = 0
    var buf = ByteBuffer.allocate(1024)
    val zero: Byte = 0
    val input = ArrayList<String>()

    fun connectAndIntroduce(token: String){
        client.connect(0 , true)
        client.introduce(ClientMessages.initMessage(token))
    }
    fun read(){
        length = client.conn.read(buf)
        if (length > 0){
            for (i in buf.array()){
                if (i == zero){
                    break
                }
                index++

            }
            input.add(String(Messages.stripBufferToByteArray(buf, index)))
            buf = ByteBuffer.allocate(1024)
            index = 0
        }

    }
    fun write(content: String){
        client.conn.write(ByteBuffer.wrap(content.toByteArray()))
    }
    fun listen(){
        while (flag){
            read()
        }
    }
    fun listenAndWriteRandom(){
        flag = true
        while (flag){
            client.conn.write(ByteBuffer.wrap(System.currentTimeMillis().toString().toByteArray()))
            read()
            sleep(1)
        }
    }
//    override fun run() {
//        super.run()
//        if (flag){
//            listen()
//        }else{
//            listenAndWriteRandom()
//        }
//        while (client.conn.isOpen && flag){
//            read()
////            write()
//        }
    }

