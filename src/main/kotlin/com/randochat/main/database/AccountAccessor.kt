package com.randochat.main.database

import java.lang.StringBuilder
import java.util.Base64
import java.util.function.ToDoubleBiFunction

class AccountAccessor (val accountRepository: AccountRepository){
    companion object {
        fun encodeAccount(string: String): String{
            return Base64.getEncoder().encodeToString(string.toByteArray())
        }
        fun decodeAccount(accountString: String): String {
            return String(Base64.getDecoder().decode(accountString))
        }
        //this method might be better off outside comapnion object
        fun stringToAccount(account: String): Account{
            val temp = account.toCharArray()
            // escape = "%"

            return TODO()
        }

    }
    //todo, put all methods that help validate and acsess data in here

    fun correctPassword(): Nothing = TODO()

}