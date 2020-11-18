package com.randochat.main.database

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
            // escape = "%"

            var start = 0
            val extractedAccount = Account()
            for (i in temp.indices){
                if (temp[i] == '%'){
                    println("entry")
                    for (j in start .. i){
                        print(temp[j])
                    }
                    start = i + 1
                }
            }

            return Account()
        }

    }
    //todo, put all methods that help validate and acsess data in here

    fun correctPassword(): Nothing = TODO()

}