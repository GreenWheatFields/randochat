package com.randochat.main.endpoints

import com.randochat.main.database.Account
import com.randochat.main.database.AccountFormatter
import com.randochat.main.database.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class RegisterAccount @Autowired constructor(final val accountRepo: AccountRepository) {
//    val accountFormatter: AccountFormatter = AccountFormatter(accountRepo)

    @RequestMapping("/accounts/create")
    fun register(@RequestHeader("newAccount", required = true) newAccount: String): String {

        val acc = AccountFormatter.stringToAccount(AccountFormatter.decodeAccount(newAccount)) //?: invalid account error. 404?
        if (acc == null) {
            println("NULL")
        }
        return "registered"
    }

}