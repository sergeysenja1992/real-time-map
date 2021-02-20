package pp.ua.ssenko.rsoket.domain

class User(
    val email: String,
    val name: String,
    var caption: String,
    var picture: String?
) : StorableEntity()
