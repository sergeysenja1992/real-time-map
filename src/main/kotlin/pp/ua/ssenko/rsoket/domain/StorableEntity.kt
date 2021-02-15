package pp.ua.ssenko.rsoket.domain

import pp.ua.ssenko.rsoket.utils.uniqId

open class StorableEntity {
    val key = uniqId();
}
