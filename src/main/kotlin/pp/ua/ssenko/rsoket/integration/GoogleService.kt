package pp.ua.ssenko.rsoket.integration

import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Component
class GoogleService() {

    val client: WebClient = WebClient.create("https://oauth2.googleapis.com/");

    suspend fun getUser(idToken: String) = client.get()
        .uri("tokeninfo?id_token=${idToken}")
        .retrieve().bodyToMono<GoogleUser>().awaitFirst()

}

data class GoogleUser(
    val email: String,
    val name: String,
    val picture: String
)
