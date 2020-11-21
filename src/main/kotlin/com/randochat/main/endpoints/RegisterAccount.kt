package com.randochat.main.endpoints

import com.randochat.main.database.AccountFormatter
import com.randochat.main.database.AccountRepository
import com.randochat.main.values.AuthCodes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest


@RestController
class RegisterAccount @Autowired constructor(final val accountRepo: AccountRepository) {
//    val accountFormatter: AccountFormatter = AccountFormatter(accountRepo)

    @RequestMapping("/accounts/create")
    fun register(@RequestHeader("newAccount", required = true) newAccount: String,
                 @RequestHeader("code", required = true) code: String): String {

        val acc = AccountFormatter.stringToAccount(AccountFormatter.decodeAccount(newAccount))
        if (acc == null) {
            println("NULL")
            //return 404?
        }
        else{
            println("here")
            println(accountRepo.findByUserName(acc.userName))
        }
        return "registered"
    }

}