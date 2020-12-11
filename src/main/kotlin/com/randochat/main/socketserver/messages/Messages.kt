package com.randochat.main.socketserver.messages

import java.io.StringReader
import java.nio.ByteBuffer
import javax.json.*
import javax.json.stream.JsonParsingException

open class Messages {
    //intent: what the server wants the client to do/ what the client wants the server to do
    // depending on the intent the rest of the json is different
    //content: content of a message a string for now
    //status: status of a room? wheter or not there is a disconnect/ user left the app but still connected/ room id
        companion object{
        fun messageToBuffer(string: String): ByteBuffer{
            return ByteBuffer.wrap(string.toByteArray())
        }
        fun messageFromBuffer(buf: ByteBuffer): JsonObject {
            val sb = StringBuilder()
            for (byte in buf.array()) {
                sb.append(byte.toChar())
            }
            return try {
                Json.createReader(StringReader(sb.toString())).readObject()
            }catch (e: JsonParsingException){
                JsonObject.EMPTY_JSON_OBJECT
            }
        }
    }


        }
