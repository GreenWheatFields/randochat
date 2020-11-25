package com.randochat.main


class playground {
    fun test(){
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
        println(attempts)
        println((System.currentTimeMillis() - startTime))
    }
}

fun main() {
    repeat(1000) {
        Thread {
            playground().test()
        }.start()
    }


}