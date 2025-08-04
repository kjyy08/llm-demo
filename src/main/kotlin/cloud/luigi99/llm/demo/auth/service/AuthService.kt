package cloud.luigi99.llm.demo.auth.service

import cloud.luigi99.llm.demo.auth.dto.AuthResponse
import cloud.luigi99.llm.demo.auth.dto.LoginRequest
import cloud.luigi99.llm.demo.auth.dto.RegisterRequest
import cloud.luigi99.llm.demo.user.domain.dto.UserResponse
import cloud.luigi99.llm.demo.user.domain.entity.User
import cloud.luigi99.llm.demo.user.enums.Role
import cloud.luigi99.llm.demo.user.repository.UserRepository
import cloud.luigi99.llm.demo.utils.jwt.JwtProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtProvider: JwtProvider
) {

    fun register(request: RegisterRequest): AuthResponse {
        require(!userRepository.existsByEmail(request.email)) {
            "이미 존재하는 이메일입니다"
        }

        val user = User.create(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            name = request.name,
            role = Role.MEMBER
        )

        val savedUser = userRepository.save(user)
        return createAuthResponse(savedUser)
    }

    fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("존재하지 않는 사용자입니다")

        require(passwordEncoder.matches(request.password, user.password)) {
            "비밀번호가 일치하지 않습니다"
        }

        return createAuthResponse(user)
    }

    private fun createAuthResponse(user: User): AuthResponse {
        val accessToken = jwtProvider.generateAccessToken(user.email)
        val refreshToken = jwtProvider.generateRefreshToken(user.email)

        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = jwtProvider.getAccessTokenExpirationMs(),
            user = UserResponse(
                id = user.id,
                email = user.email,
                name = user.name,
                role = user.role
            )
        )
    }
}