package com.randochat.main.socketserver

//chat room data
class Room {
    companion object{
        //static method that generates room objects?
        fun generateRoom(id: String, members: Array<Any>, startTime: Long,
                        nextVote: Long, prompt: String): Room = TODO()
//        id: String, uuid
//        members: array of ip addresses? / account Obejcts
//        startTime: timestamp. either Room generation or first transported message
//        nextVote: when the users are able to vote on to continue the chat or reveal the profiles
//        prompt: randomly chosen prompt. a list the choose from should not be replicated

    }


    //called when room is closed, save the room somewhere?
    fun save(): Nothing = TODO()



}