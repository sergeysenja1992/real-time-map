package pp.ua.ssenko.rsoket.config

import com.fasterxml.jackson.annotation.JsonTypeName
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.NamedType
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.context.annotation.Configuration
import org.springframework.core.type.filter.AssignableTypeFilter
import pp.ua.ssenko.rsoket.domain.User
import pp.ua.ssenko.rsoket.repository.FileStorage
import pp.ua.ssenko.rsoket.repository.Storage
import ua.pp.ssenko.chronostorm.domain.MapObject

@Configuration
class AppConfig {
    @Bean
    fun locationMapsDirectory() = "${System.getProperty("user.home")}/.real-time-map/"

    @Bean
    fun objectMapper(): ObjectMapper = objectMapper.value

    @Bean
    fun userStorage(locationMapsDirectory: String): Storage<User> = FileStorage(locationMapsDirectory, "user", User::class.java)
}






private val elements: List<String> = ArrayList()
private val objectMapper = lazy {
    val mapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    val provider = ClassPathScanningCandidateComponentProvider(false)
    provider.addIncludeFilter(AssignableTypeFilter(MapObject::class.java))

    val components = provider.findCandidateComponents("ua/pp/ssenko")
    for (component in components) {
        val cls = Class.forName(component.beanClassName)
        val jsonTypeName = cls.getAnnotation(JsonTypeName::class.java)
        mapper.registerSubtypes(NamedType(cls, jsonTypeName.value))
        @Suppress("UNCHECKED_CAST")
        (elements as MutableList<String>).add(jsonTypeName.value)
    }

    mapper
}

fun objectMapper() = objectMapper.value
