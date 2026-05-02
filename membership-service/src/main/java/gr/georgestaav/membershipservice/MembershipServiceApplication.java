package gr.georgestaav.membershipservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients
@OpenAPIDefinition(
        info = @Info(
                title = "Membership Microservice REST API Documentation",
                description = "Microservice for managing athlete memberships and medical certificates",
                version = "v1"
        )
)
public class MembershipServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MembershipServiceApplication.class, args);
    }

}
