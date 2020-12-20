package test_socket_server

import com.randochat.main.socketserver.messages.ClientMessages
import java.nio.ByteBuffer
import javax.json.JsonObject

//what an actual client will look like. listening and talking on two threaders
class MultiThreadedClient(val client: Client): Thread() {
    var flag = true
    val output = ArrayList<JsonObject>()
    fun connectAndIntroduce(){
        client.connect(0 , true)
        client.introduce(ClientMessages.initMessage)
    }
    fun read(){

    }
    fun write(buf: ByteBuffer){

    }

    override fun run() {
        super.run()
//        while (client.conn.isOpen && flag){
//            read()
////            write()
//        }
    }
}