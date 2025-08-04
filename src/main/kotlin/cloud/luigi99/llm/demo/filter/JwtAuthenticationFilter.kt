package cloud.luigi99.llm.demo.filter

import cloud.luigi99.llm.demo.user.repository.UserRepository
import cloud.luigi99.llm.demo.utils.jwt.JwtProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtProvider,
    private val userRepository: UserRepository
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val jwt = extractJwtFromRequest(request)

            jwt?.takeIf { jwtTokenProvider.validateToken(it) }?.let { token ->
                val email = jwtTokenProvider.getEmailFromToken(token)

                userRepository.findByEmail(email)?.let { user ->
                    val authorities = listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))

                    val authentication = UsernamePasswordAuthenticationToken(
                        user.id,
                        null,
                        authorities
                    )

                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        } catch (ex: Exception) {
            log.error("사용자 인증을 설정할 수 없습니다", ex)
        }

        filterChain.doFilter(request, response)
    }

    private fun extractJwtFromRequest(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")
            ?.takeIf { it.startsWith("Bearer ") }
            ?.substring(7)
    }
}