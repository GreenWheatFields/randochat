package com.randochat.main.database

import org.springframework.data.repository.CrudRepository


interface AccountRepository : CrudRepository<Account, String>{
    fun findByUserName(username: String): Account?
    fun findByEmail(email: String): Account?
}

//authenticate accounts, get account profiles?