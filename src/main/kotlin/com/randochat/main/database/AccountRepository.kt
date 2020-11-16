package com.randochat.main.database

import org.springframework.data.repository.CrudRepository


interface AccountRepository : CrudRepository<Account, String>{
//    fun findByEmail(email: String): List<Account>
    //todo, ^ breaks for some reason
}

//authenticate accounts, get account profiles?