package com.randochat.main.endpoints

import com.randochat.main.database.AccountAccessor
import com.randochat.main.database.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.lang.NonNull
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RegisterAccount @Autowired constructor(final val accountRepo: AccountRepository) {
    val accountAccessor: AccountAccessor = AccountAccessor(accountRepo)


    @RequestMapping("/accounts/create")
    fun register(@RequestHeader("newAccount", required = false) newAccount: String): String {

        //userAgent and contains the key in the app.
//        println(accountRepository.findByUserName("username"))
        return "registered"
    }

}