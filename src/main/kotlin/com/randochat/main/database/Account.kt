package com.randochat.main.database

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


//todo, create this table in the db
@Entity
@Table(name = "accounts")
class Account {
    //id == hashed account name or account name?
    @Id
    @Column
    lateinit var email: String
    @Column
    lateinit var userName: String
    @Column
    lateinit var password: String
    //todo later, login attempts/locations/ip addreses etc?

}