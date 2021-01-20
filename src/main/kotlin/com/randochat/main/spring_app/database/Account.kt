package com.randochat.main.spring_app.database

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.servlet.http.HttpServletRequest
import org.json.*


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
    lateinit var username: String
    @Column
    lateinit var password: String
    @Column
    lateinit var bio: String
    @Column
    lateinit var location: String //gneralized location and closer location somewhere else
    @Column
    var imageLink: String? = null
    @Column
    lateinit var accountStatus: String //ok, locked , banned
    @Column
    lateinit var loginAttempts: String //string representation of json
//    @Column
//    lateinit var blockedUsers: arrat of account ids
    fun addLoginAttempt(request: HttpServletRequest): Boolean {
        val json: JSONObject

        if (this::loginAttempts.isInitialized){
            json = JSONObject(loginAttempts)
            //todo, increment values, catch parse error
            // this method won't be necessary till a bit later

        }else{
            json = JSONObject().also {
                it.put("totalAttempts", 1)
                it.put("rolling24HourAttempts", 1)
                it.put("rolling1hourAttempts", 1)
                it.put("lastCheck", System.currentTimeMillis())
                it.put("attemptLog", "null") //nested json
            }
        }

    //ip address, time, total
        //increment
        return (json.get("rolling24HourAttempts") as Int) < 3
    }
    fun getProtectedAccountData(includeEmail: Boolean = false): Map<Any, Any?> {
        println(username)
        return mapOf(
                "id" to accountID,
                "username" to username,
                "bio" to bio,
                "imageLink" to imageLink,
                "status" to accountStatus //todo, this inst being intialized when it exist in the databse
        )

    }


}