package com.randochat.main.database


class AccountAccessor (val accountRepository: AccountRepository){
    //todo, put all methods that help validate and acsess data in here
    fun tset1(){
        println(accountRepository.findByUserName("lol"))
    }
    fun correctPassword(): Nothing = TODO()

}