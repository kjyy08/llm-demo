package cloud.luigi99.llm.demo.utils.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtProvider(
    @Value("\${jwt.secret}") secret: String,
    @Value("\${jwt.access-token-expiration-ms}") private val accessTokenExpirationMs: Long,
    @Value("\${jwt.refresh-token-expiration-ms}") private val refreshTokenExpirationMs: Long
) {
    private val log = LoggerFactory.getLogger(JwtProvider::class.java)
    
    private val secretKey: SecretKey = run {
        val keyBytes = Base64.getDecoder().decode(secret)
        Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateAccessToken(email: String): String {
        val now = Date()
        val expiryDate = Date(now.time + accessTokenExpirationMs)
        
        return Jwts.builder()
            .subject(email)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey)
            .compact()
    }

    fun generateRefreshToken(email: String): String {
        val now = Date()
        val expiryDate = Date(now.time + refreshTokenExpirationMs)
        
        return Jwts.builder()
            .subject(email)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey)
            .compact()
    }

    fun getEmailFromToken(token: String): String {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: SecurityException) {
            log.warn("JWT 서명이 유효하지 않습니다: {}", e.message)
            false
        } catch (e: MalformedJwtException) {
            log.warn("JWT 토큰이 올바르지 않습니다: {}", e.message)
            false
        } catch (e: ExpiredJwtException) {
            log.warn("JWT 토큰이 만료되었습니다: {}", e.message)
            false
        } catch (e: UnsupportedJwtException) {
            log.warn("지원하지 않는 JWT 토큰입니다: {}", e.message)
            false
        } catch (e: JwtException) {
            log.error("기타 JWT 토큰 오류: {}", e.message)
            false
        } catch (e: IllegalArgumentException) {
            log.warn("JWT 토큰이 잘못되었습니다: {}", e.message)
            false
        }
    }

    fun getAccessTokenExpirationMs(): Long = accessTokenExpirationMs

    fun getRefreshTokenExpirationMs(): Long = refreshTokenExpirationMs
}
