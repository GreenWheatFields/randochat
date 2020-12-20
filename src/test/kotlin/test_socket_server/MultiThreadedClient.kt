package test_socket_server

import antlr.debug.MessageAdapter
import com.randochat.main.socketserver.messages.ClientMessages
import com.randochat.main.socketserver.messages.Messages
import java.awt.desktop.SystemSleepEvent
import java.nio.ByteBuffer
import javax.json.JsonObject

//what an actual client will look like. listening and talking on two threaders
class MultiThreadedClient(val client: Client): Thread() {
    var flag = true
    var length = -1
    var index = 0
    var buf = ByteBuffer.allocate(1024)
    val zero: Byte = 0
    val input = ArrayList<String>()
    fun connectAndIntroduce(){
        client.connect(0 , true)
        client.introduce(ClientMessages.initMessage)
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
    override fun run() {
        super.run()
        listen()
//        while (client.conn.isOpen && flag){
//            read()
////            write()
//        }
    }
}
