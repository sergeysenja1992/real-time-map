package pp.ua.ssenko.rsoket

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.util.MimeTypeUtils
import org.springframework.web.reactive.function.client.WebClient
import pp.ua.ssenko.rsoket.domain.User
import pp.ua.ssenko.rsoket.repository.UserRepository
import pp.ua.ssenko.rsoket.utils.log
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.lang.Thread.sleep
import java.net.URI
import java.time.Duration

@SpringBootTest
class RSocketApplicationTests {

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun test() {
        log.info("start")
        userRepository.save(User("email1", "name"))
        sleep(201)
        log.info("start loop")
        for (i in 0..100) {
            userRepository.save(User("email2-${i}", "name"))
        }
        log.info("end loop")
        sleep(101)
        for (i in 0..100) {
            userRepository.save(User("email3-${i}", "name"))
        }
        sleep(100)
        for (i in 0..100) {
            userRepository.save(User("email4-${i}", "name"))
        }
    }



}
