package com.randochat.main.spring_app.utility

//request key for socket sever. maybe talk to socket sever here?
import com.auth0.jwt.*
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import com.randochat.main.spring_app.database.Account
import com.randochat.main.spring_app.values.AuthCodes
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

    fun genAccountToken(account: Account): String {
        //take in an account entity and parse it?
        val token = JWT.create().withIssuer("accountServer")
                .withClaim("id", account.accountID)
                .withClaim("status", "temporary ban") //account.status
                .withExpiresAt(Date(System.currentTimeMillis() + 10_000))
                .sign(algo)
        return token
    }
    fun checkToken(token: String): DecodedJWT? {
        //may need to return more information. return response entities from here?
        val validator = JWT.require(algo).withIssuer("accountServer").acceptLeeway(1).build()
        try {
            validator.verify(token)
        }catch (e: JWTDecodeException){
            return null
        }catch (e: TokenExpiredException){
            return null
        }
        return JWT.decode(token)
    }

    fun getSocketToken(): Nothing = TODO()

}

fun main() {
//    Token().checkToken(Token().genAccountToken())

}