package test_socket_server

import com.randochat.main.socketserver.messages.ClientMessages
import com.randochat.main.socketserver.messages.Messages
import java.nio.ByteBuffer
import javax.json.JsonObject

//what an actual client will look like. listening and talking on two threaders
class MultiThreadedClient(val client: Client): Thread() {
    var flag = true
    var length = 0
    var buf = ByteBuffer.allocate(1024)
    val input = ArrayList<String>()
    fun connectAndIntroduce(){
        client.connect(0 , true)
        client.introduce(ClientMessages.initMessage)
    }
    fun read(){
        length = client.conn.read(buf)
        if (length > 0){
            input.add(String(Messages.stripBufferToByteArray(buf, length)))
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
    override fun run() {
        super.run()
        listen()
//        while (client.conn.isOpen && flag){
//            read()
////            write()
//        }
    }
}
