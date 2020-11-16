package com.randochat.main.database

import org.springframework.data.repository.CrudRepository

interface AccountRepository : CrudRepository<Account, String>{
    fun findByEmail(email: String): List<Account>
}

//authenticate accounts, get account profiles?