package pp.ua.ssenko.rsoket.service

import org.springframework.stereotype.Service
import pp.ua.ssenko.rsoket.domain.User
import pp.ua.ssenko.rsoket.integration.GoogleService
import pp.ua.ssenko.rsoket.repository.UserRepository
import pp.ua.ssenko.rsoket.web.LoginDto

@Service
class UserService(
    val userRepository: UserRepository,
    val googleService: GoogleService
) {

    suspend fun login(login: LoginDto): User {
        val googleUser = googleService.getUser(login.idToken)
        val user = User(googleUser.email, googleUser.name, googleUser.name, googleUser.picture)
        val existingUser = userRepository.findByEmail(user.email)
        if (existingUser != null) {
            userRepository.save(existingUser)
            return existingUser
        }
        userRepository.save(user)
        return user
    }

}
