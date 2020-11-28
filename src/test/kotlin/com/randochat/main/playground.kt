package com.randochat.main

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger


class playground {
    fun test(threadCounter: AtomicInteger){
        val limit = Int.MAX_VALUE
        val random = java.util.Random()
//        println(random.nextLong())
        var guess1 = random.nextInt(limit)
        var guess2 = random.nextInt(limit)
        var attempts = 0
        val startTime = System.currentTimeMillis()
        if (guess1 == guess2){
            println("first try")
            System.exit(0)
        }
        while (true){

            if (guess1 == guess2){
                break
            }else{
                attempts++
                guess1 = random.nextInt(limit)
                guess2 = random.nextInt(limit)

            }
        }
        println(threadCounter.decrementAndGet())
//        println(attempts)
//        println((System.currentTimeMillis() - startTime))
//        val threadCount = AtomicInteger(1000)
//        repeat(1000) {
//            val thread = Thread {
//                playground().test(threadCount)
//            }
//            thread.start()
//        }
    }
}

fun main() {

//    val temp = ConcurrentHashMap<Int, ConcurrentHashMap<Int, Object>>()
//    val t = HashMap<Int, Any?>()
//    val i = null
//    temp[5]!!.put(5, null)
//    temp[5]?.put(0, i)

}