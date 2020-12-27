package com.randochat.main.spring_app.database

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


//todo, create this table in the db
@Entity
@Table(name = "accounts")
class Account{
    //todo, account Ids should be random numbers
    //id == hashed account name or account name?
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
    lateinit var location: String
    @Column
    lateinit var imageLink: String
    @Column
    lateinit var accountStatus: String //clear, locked (too many sign on attempts), banned
    @Column
    lateinit var loginAttempts: String //string representation of json
// totalAttempts: int, rolling24hourAttempts: int, rolling one hours Attempts:, lastCheck: timestamp
// time:timestamp, ip: ipaddres, location:}

}