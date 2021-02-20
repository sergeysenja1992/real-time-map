package pp.ua.ssenko.rsoket.config

import com.fasterxml.jackson.annotation.JsonTypeName
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.NamedType
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.context.annotation.Configuration
import org.springframework.core.type.filter.AssignableTypeFilter
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import pp.ua.ssenko.rsoket.domain.User
import pp.ua.ssenko.rsoket.repository.AsyncFileStorage
import pp.ua.ssenko.rsoket.repository.Storage
import pp.ua.ssenko.rsoket.web.getCurrentUser
import reactor.core.publisher.Mono
import ua.pp.ssenko.chronostorm.domain.MapObject


@Configuration
@OpenAPIDefinition(info = Info(title = "APIs", version = "1.0", description = "Documentation APIs v1.0"))
class AppConfig {

    @Bean
    fun locationMapsDirectory() = "${System.getProperty("user.home")}/.real-time-map/"

    @Bean
    fun objectMapper(): ObjectMapper = objectMapper.value

    @Bean
    fun userStorage(locationMapsDirectory: String): Storage<User>
        = AsyncFileStorage(locationMapsDirectory, "user", User::class.java)

}

@Component
class PermissionWebFilter : WebFilter {
    override fun filter(
        exchange: ServerWebExchange,
        webFilterChain: WebFilterChain
    ): Mono<Void> {
        return exchange.session.flatMap {
            if (it.getCurrentUser() == null && exchange.request.path.toString() != "/api/user/login") {
                val response = exchange.getResponse();
                response.setStatusCode(UNAUTHORIZED);
                response.setComplete()
            } else {
                webFilterChain.filter(exchange)
            }
        }
    }
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
