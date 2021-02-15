package pp.ua.ssenko.rsoket.domain

class User(
    val email: String,
    val name: String,
    val refreshToken: String? = null
) : StorableEntity()
