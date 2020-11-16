package com.randochat.main.database

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id


//todo, create this table in the db
@Entity
class Account {
    //id == hashed account name or account name?
    @Id
    @Column
    lateinit var hashedAccountName: String
    @Column
    lateinit var hashedPassword: String //todo, char array can be stored in db?
    //todo later, login attempts/locations/ip addreses etc?

}