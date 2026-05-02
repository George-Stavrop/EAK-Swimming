package gr.georgestaav.gatewayserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.cloud.config.enabled=false",
        "spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/test"
})
class GatewayserverApplicationTests {

    @Test
    void contextLoads() {
    }

}
