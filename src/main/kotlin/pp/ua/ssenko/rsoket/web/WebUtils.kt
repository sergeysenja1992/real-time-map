package pp.ua.ssenko.rsoket.web

import org.springframework.web.server.WebSession
import pp.ua.ssenko.rsoket.domain.User

fun WebSession.login(user: User) {
    attributes.put("USER", user)
}

fun WebSession.logout() {
    attributes.remove("USER")
}

fun WebSession.getCurrentUser(): User? {
    return attributes.get("USER") as User?
}
