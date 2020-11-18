package com.randochat.main.database

import java.lang.IndexOutOfBoundsException
import java.util.*
import kotlin.collections.HashSet

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
            var extractedValues = 0
            val extractedAccount = Account()
            val sb = StringBuilder()

            for (i in temp.indices){
                if (temp[i] == '\\'){
                    for (j in start until i){
                        sb.append(temp[j])
                    }
                    //todo, this can be simplified, drys
                    /*when(extractedValues){
                        0 -> {
                          if (validEmail(sb.toString())){
                              extractedAccount.email = sb.toString()
                          }else{
                              //throw NullPointerException()
                              //todo, handle invalid email
                          }
                        }
                        1 -> {
                            //if validUserName
                        }
                    }*/
                    start = i + 1
                }
            }
            //todo, validate email adresses username passwords before defining account object varibles
            return Account()
        }
        fun validEmail(email: String): Boolean{
            //find @, make sure it isn't proceded by dots?
            //find .com or other ending
            var start = 0
            val validCharsAll = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-!#\$%&'*+/=?^_`{|}~".toCharArray()
            //todo dont forget to add '.'
            val validCharSetEmail = HashSet(listOf(validCharsAll))

            for (i in email.indices + 1){
                if (i == email.length + 1){
                    //todo check domaio
                }
                else if (email[i] == '@'){
                    for(j in email.slice(start..i - 1)){
                        if (!validCharSetEmail.contains(j)){
                            //invalid email?
                            //check for two dots ..
                        }
                    }
                }else if (email[i] == '.' && start != 0){
                    //todo, check domain
                }
            }
            return false

        }

    }
    //todo, put all methods that help validate and acsess data in here

    fun correctPassword(): Nothing = TODO()

}