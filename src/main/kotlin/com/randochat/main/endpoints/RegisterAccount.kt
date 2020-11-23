package com.randochat.main.endpoints

import com.randochat.main.database.AccountFormatter
import com.randochat.main.database.AccountRepository
import com.randochat.main.values.AuthCodes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest


@RestController
class RegisterAccount @Autowired constructor(final val accountRepo: AccountRepository) {
//    val accountFormatter: AccountFormatter = AccountFormatter(accountRepo)

    @RequestMapping("/accounts/create")
    fun register(@RequestHeader("newAccount", required = true) newAccount: String,
                 @RequestHeader("code", required = true) code: String): ResponseEntity<Any> {

        val acc = AccountFormatter.stringToAccount(AccountFormatter.decodeAccount(newAccount)) ?: return ResponseEntity("bad account", HttpStatus.UNAUTHORIZED)
        if ((!AuthCodes.codes.contains(code))) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
        println(accountRepo.existsById(acc.email))

        return ResponseEntity("registered", HttpStatus.OK)
    }

}