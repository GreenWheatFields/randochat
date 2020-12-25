package com.randochat.main.springapp.utility

//request key for socket sever. maybe talk to socket sever here?
import com.auth0.jwt.*
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.randochat.main.springapp.values.AuthCodes
import java.util.*

/*
token expires every hour
account data should be seperated to public progiles and information like chats etc that can only be acsessed bu that accoun
payload: accountId, isAccountBanned/account acsess: int?, ipaddress when token generated?

*/



class Token {
    //pass in account object from database?
    //todo algo and validator as singletons?
    val algo = Algorithm.HMAC256(AuthCodes.key)

    fun genAccountToken(): String {
        //take in an account entity and parse it?
        val token = JWT.create().withIssuer("test")
                .withClaim("id", "accountID")
                .withClaim("status", "temporary ban")
                .withExpiresAt(Date(System.currentTimeMillis()))
                .sign(algo)
        return token
    }
    fun checkToken(token: String): Boolean {
        //may need to return more information. return response entities from here?
        val validator = JWT.require(algo).withIssuer("test").acceptLeeway(1).build()
        try {
            validator.verify(token)
        }catch (e: JWTDecodeException){
            return false
        }catch (e: TokenExpiredException){
            return false
        }
        val clearToken = JWT.decode(token)
        println(clearToken.getClaim("status").asString())

        return true
    }

    fun getSocketToken(): Nothing = TODO()

}

fun main() {
    Token().checkToken(Token().genAccountToken())

}