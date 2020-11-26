package com.randochat.main.socketserver

import org.aspectj.apache.bcel.generic.InstructionConstants
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.*
import java.nio.ByteBuffer
import java.nio.channels.*
import java.nio.channels.ServerSocketChannel
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.typeOf


// the directory of connections should be an object that is never replicated and modified by one class.
// starting with a simple text chat
class AcceptConnections: Thread() {
    fun listen(){
        val selector = Selector.open()
        val server = ServerSocketChannel.open()
        val directory = ConcurrentHashMap<SocketChannel, String>()
        val readJobs = ConcurrentLinkedQueue<SocketChannel>()
        server.configureBlocking(false)
        server.socket().bind(InetSocketAddress("localhost", 15620))
        server.register(selector, SelectionKey.OP_ACCEPT)
        //maybe host queues in this thread?
        val clientHandler = ClientHandler(directory, readJobs)
        clientHandler.start()
        while (true){
            selector.select()
            val keys = selector.selectedKeys().iterator()
            while (keys.hasNext()){
                val key = keys.next() as SelectionKey
                keys.remove()
                if (key.isAcceptable){
                    println("accepting")
                    //use a pool here to handle db calls
                    //this thread should just determine whether a connection is valid.
                    val channel = key.channel() as ServerSocketChannel
                    val newChan = channel.accept()
                    newChan.configureBlocking(false)
                    newChan.register(selector, SelectionKey.OP_READ, SelectionKey.OP_WRITE)
                    println(newChan.hashCode())
                    directory[newChan] = "connection"

                }
                if (key.isReadable){
                    println("readable")
                    println(key.channel().hashCode())
                    readJobs.add(key.channel() as SocketChannel)

                    val channel = key.channel() as SocketChannel
                    val buffer = ByteBuffer.allocate(1024)
                    var mesLen = -1
                    mesLen = channel.read(buffer)
                    if (mesLen > -1){
                        println(String(Arrays.copyOfRange(buffer.array(), 0, mesLen)))
                }

                }


            }
        }

    }

    override fun run() {
        super.run()
        listen()
    }
}

fun main() {
    val acceptConns = AcceptConnections()
    acceptConns.start()
    repeat(1){
        val client = Client()
        client.start()
    }





}