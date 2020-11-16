package com.randochat.main.database

import org.springframework.beans.factory.annotation.Autowired

class AccountService {
    @Autowired
    //todo, not being initialized
    private lateinit var accountRepo: AccountRepository
    fun test1(){
//        accountRepo.
        println("here")
        accountRepo.findAll()
    }
}