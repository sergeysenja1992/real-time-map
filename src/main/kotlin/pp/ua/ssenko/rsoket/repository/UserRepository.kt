package pp.ua.ssenko.rsoket.repository

import pp.ua.ssenko.rsoket.domain.User

class UserRepository(
    private val storage: Storage<User>
) {

    fun getByKey(key: String) = storage.get(key)

    fun findByEmail(email: String): User? {
        return storage.getAll().filter { it.email == email }.firstOrNull()
    }

}
