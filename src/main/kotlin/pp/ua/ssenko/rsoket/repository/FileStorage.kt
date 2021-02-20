package pp.ua.ssenko.rsoket.repository

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.readValue
import pp.ua.ssenko.rsoket.config.objectMapper
import pp.ua.ssenko.rsoket.domain.StorableEntity
import java.io.File

open class FileStorage<T: StorableEntity>(
    val locationMapsDirectory: String, val name: String, val type: Class<T>
): Storage<T> {

    @Volatile
    private var storage: MutableMap<String, T> = HashMap();

    override fun loadStorage() {
        val filePath = getFilePath()
        val storageFile = File(filePath)
        if (storageFile.exists()) {
            val storageValue = storageFile.readText()
            if (storageValue.isNotBlank()) {
                val value: List<T> = objectMapper().readValue(storageValue)
                this.storage = HashMap(value.map { it.key to it }.toMap());
            }
        }
    }

    private fun getFilePath(): String {
        var locationMapsDirectory = this.locationMapsDirectory
        if (!locationMapsDirectory.endsWith("/")) {
            locationMapsDirectory = locationMapsDirectory + "/"
        }
        File(locationMapsDirectory).mkdirs();
        val filePath = "$locationMapsDirectory/$name"
        return filePath
    }

    override fun get(key: String) = storage.get(key)

    override fun getAll(): Collection<T> = storage.values

    override fun update(value: T) {
        storage.put(value.key, value);
        store();
    }

    override fun update(values: List<T>) {
        this.storage.putAll(HashMap(values.map { it.key to it }.toMap()));
        store();
    }

    override fun store() {
        val value = objectMapper().writeValueAsString(storage.values)
        File(getFilePath()).writeText(value)
    }
}


