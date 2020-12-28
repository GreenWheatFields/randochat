package com.randochat.main.spring_app.database

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


//todo, create this table in the db
@Entity
@Table(name = "accounts")
class Account{
    //todo, method that takes an account and returns only the public info? never return account objects directly!!
    @Id
    @Column
    lateinit var accountID: String
    @Column
    lateinit var email: String
    @Column
    lateinit var userName: String
    @Column
    lateinit var password: String
    @Column
    lateinit var bio: String
    @Column
    lateinit var location: String //gneralized location and closer location somewhere else
    @Column
    lateinit var imageLink: String
    @Column
    lateinit var accountStatus: String //clear, locked (too many sign on attempts), banned
    @Column
    lateinit var loginAttempts: String //string representation of json
// totalAttempts: int, rolling24hourAttempts: int, rolling one hours Attempts:, lastCheck: timestamp
// time:timestamp, ip: ipaddres, location:}
    fun addLoginAttempt(){
//        if ()
        // return wheter or not to lock the account
    }
    fun getProtectedAccountData(includeEmail: Boolean = false) {
        //map of account data
    }


}