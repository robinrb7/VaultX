package banking.BankingProject.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import java.util.Base64
import java.util.Date



@Component
class JwtUtil(
    @Value("\${jwt.access-expiration-ms}")
    private val accessExpirationMs: Long,

    @Value("\${jwt.refresh-expiration-ms}")
    private val refreshExpirationMs: Long,

    @Value("\${jwt.issuer}")
    private val issuer: String,

    @Value("\${jwt.secret}")
    private val secret: String,
) {

    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret))

    fun generateAccessToken(username: String, customerId: Long): String{
        val now = Date()
        val expiry = Date(now.time + accessExpirationMs)

        return Jwts.builder()
            .subject(username)
            .issuer(issuer)
            .issuedAt(now)
            .expiration(expiry)
            .claim("cid",customerId)
            .signWith(secretKey,Jwts.SIG.HS256)
            .compact()
    }

    fun generateRefreshToken(username: String, customerId:Long): String{
        val now = Date()
        val expiry = Date(now.time + refreshExpirationMs)

        return Jwts.builder()
            .subject(username)
            .issuer(issuer)
            .issuedAt(now)
            .expiration(expiry)
            .claim("cid",customerId)
            .signWith(secretKey,Jwts.SIG.HS256)
            .compact()
    }

     fun getUsernameFromToken(token : String): String{
         val claims = getAllClaims(token) ?: throw ResponseStatusException(HttpStatus.valueOf(401),"Invalid Token.")
        return claims.subject
    }

    fun getCustomerIdFromToken(token : String): Long{
        val claims = getAllClaims(token) ?: throw ResponseStatusException(HttpStatus.valueOf(401),"Invalid Token.")
        val rawValue =  claims["cid"]

        return when (rawValue){
            is Int -> rawValue.toLong()
            is Long -> rawValue
            else -> throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid CID type in token")
        }
    }

    fun validateToken(token: String) : Boolean{
        return try {
            val claim = getAllClaims(token) ?: return false
            !claim.expiration.before(Date())
        } catch(e: Exception){
            false
        }

    }

    private fun getAllClaims(token: String): Claims? {
        return try{
                    Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .payload
        } catch (e: Exception){
            println("JWT parse failed: ${e.message}")
            null
        }
    }

}