package com.randochat.main.spring_app.utility

//request key for socket sever. maybe talk to socket sever here?
import com.auth0.jwt.*
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
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



object Token {
    //pass in account object from database?
    //todo algo and validator as singletons?
    //todo should this be static
    fun strip(s: String): String{
        return s.substring(1, s.length - 1)
    }

    val algo = Algorithm.HMAC256(AuthCodes.key)

    fun genAccountToken(account: Account, expiresAt: Int = 10_0000, args: Map<String, String>? = null): String {
        //take in an account entity and parse it?
        val token = JWT.create().withIssuer("accountServer")
                .withClaim("id", account.accountID)
                .withClaim("status", "account.status") //todo lateinit error here
                .withClaim("blockList", "account.blocklist")
                .withClaim("matches", "account.matches")
                .withClaim("seeking", account.seeking)
                .withClaim("gender", account.gender)
                .withExpiresAt(Date(System.currentTimeMillis() + expiresAt))
        return token.sign(algo)
    }
    fun checkTokenValid(token: String): DecodedJWT? {
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
    fun copyToken(token: DecodedJWT): JWTCreator.Builder?{
        val newToken = JWT.create().withIssuer("accountServer").withExpiresAt(Date(System.currentTimeMillis() + 10_000))
        for (claim in token.claims.keys){
            newToken.withClaim(claim, token.getClaim(claim).asString())
        }
        return newToken
    }
    fun getSocketToken(): Nothing = TODO()
    fun sign(token: JWTCreator.Builder): String {
        return token.sign(algo)
        // wtf is this
    }
}

fun main() {

}