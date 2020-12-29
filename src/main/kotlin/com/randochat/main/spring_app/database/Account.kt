package com.randochat.main.spring_app.database

import java.io.StringReader
import java.math.BigInteger
import javax.json.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.servlet.http.HttpServletRequest
import org.json.*


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
    fun addLoginAttempt(request: HttpServletRequest){
        var json: JSONObject
        json = JSONObject("{'test':2}")
        println(json)
//        var json: JsonObjectBuilder
//        var loginAttemptsJson: Any
//        if (this::loginAttempts.isInitialized){
//            json = Json.createObjectBuilder(Json.createReader(StringReader(loginAttempts)).readObject())
//        }else{
//            json = Json.createObjectBuilder().also {
//                it.add("totalAttempts", Json.createValue(1))
//                it.add("rolling24HourAttempts", Json.createValue(1))
//                it.add("rolling1hourAttempts", Json.createValue(1))
//                it.add("lastCheck",Json.createValue(System.currentTimeMillis()))
//                it.add("attemptLog", Json.createObjectBuilder().build())
//            }
//        }
//    //ip address, time, total
//        //increment
//        val temp = json.build()
//        loginAttemptsJson = Json.createObjectBuilder(temp.get("attemptLog")!!.asJsonObject()).add("test1", "test1").build()
//        val temp2 = Json.createObjectBuilder(temp).add("attemptLog", loginAttemptsJson).build()
//        println(temp2)
//        // return wheter or not to lock the account
    }
    fun getProtectedAccountData(includeEmail: Boolean = false) {
        //map of account data
    }


}