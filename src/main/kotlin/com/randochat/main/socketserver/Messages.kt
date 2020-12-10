package com.randochat.main.socketserver

import java.nio.ByteBuffer
import javax.json.*

class Messages {
    //intent: what the server wants the client to do/ what the client wants the server to do
    // depending on the intent the rest of the json is different
    //content: content of a message a string for now
    //status: status of a room? wheter or not there is a disconnect/ user left the app but still connected/ room id

    companion object{
        fun messageToBuffer(): Nothing = TODO()
        fun messageFromBuffer(): Nothing = TODO()
    }
            class serverMessages {
                companion object{
                    fun welcomeMessage(room: Room): ByteBuffer {
                        var message = "WELCOME "
                        message += room.id
                        val temp = Json.createObjectBuilder().add("intent", "WELCOME").add("status", "null").build()
                        val status =  Json.createObjectBuilder().add("roomID", room.id.toString())
                        return ByteBuffer.wrap(message.toByteArray())
                    }
                }
            }

            class clientMessages() {
            }
        }
