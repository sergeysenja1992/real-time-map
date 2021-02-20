package pp.ua.ssenko.rsoket.web

import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import pp.ua.ssenko.rsoket.domain.User
import pp.ua.ssenko.rsoket.service.UserService

@RestController
@RequestMapping("/api/user")
class UserResource(
    val userService: UserService
) {

    @PostMapping("/login")
    suspend fun login(@RequestBody login: LoginDto, swe: ServerWebExchange) {
        val user = userService.login(login)
        swe.session.awaitFirst().login(user)
    }

    @GetMapping("/account")
    suspend fun login(swe: ServerWebExchange): User? {
        return swe.session.awaitFirst().getCurrentUser()
    }
}

data class LoginDto(
    val idToken: String
)
