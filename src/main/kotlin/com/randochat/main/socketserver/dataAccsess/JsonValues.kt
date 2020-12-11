package com.randochat.main.socketserver.dataAccsess

import javax.json.Json

//using this class removes the need for constant json.get.toString().equals("\"value\"")
class JsonValues {
    companion object{
        val OPENNEW = Json.createValue("OPENNEW")
        val RECONNECT = Json.createValue("RECONNECT")
    }


}