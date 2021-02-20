package pp.ua.ssenko.rsoket.repository

import org.springframework.stereotype.Component
import pp.ua.ssenko.rsoket.domain.User

@Component
class UserRepository(
    private val storage: Storage<User>
) {

    fun getByKey(key: String) = storage.get(key)

    fun findByEmail(email: String): User? {
        return storage.getAll().filter { it.email == email }.firstOrNull()
    }

    fun save(user: User) {
        storage.update(user)
    }

}
