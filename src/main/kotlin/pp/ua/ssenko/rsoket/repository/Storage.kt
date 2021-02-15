package pp.ua.ssenko.rsoket.repository

import pp.ua.ssenko.rsoket.domain.StorableEntity

interface Storage<T: StorableEntity> {
    fun loadStorage()
    fun update(value: T)
    fun update(value: List<T>)
    fun store()
    fun get(key: String): T?
    fun getAll(): Collection<T>
}
