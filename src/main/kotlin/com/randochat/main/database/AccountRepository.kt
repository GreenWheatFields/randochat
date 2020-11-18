package com.randochat.main.database

import org.springframework.data.repository.CrudRepository


interface AccountRepository : CrudRepository<Account, String>{
    fun findByUserName(email: String): Account?
    //todo, ^ breaks for some reason
}

//authenticate accounts, get account profiles?