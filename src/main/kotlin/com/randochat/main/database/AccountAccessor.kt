package com.randochat.main.database

import java.lang.IndexOutOfBoundsException
import java.util.*

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
            // escape = "\" todo, comments in email addresses will break this
            //staing example email@email.com\username\password\

            var start = 0
            val extractedAccount = Account()
            for (i in temp.indices){
                if (temp[i] == '\\'){
                    for (j in start until i){
                        print(temp[j])
                    }
                    println()
                    start = i + 1
                }
            }
            //todo, validate email adresses username passwords before defining account object varibles
            return Account()
        }

    }
    //todo, put all methods that help validate and acsess data in here

    fun correctPassword(): Nothing = TODO()

}