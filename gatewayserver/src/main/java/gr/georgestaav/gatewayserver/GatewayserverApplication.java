package gr.georgestaav.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class GatewayserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayserverApplication.class, args);
    }


    @Bean
    public RouteLocator eakSwimmingRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p.
                        path("/eak/membership/**")
                        .filters(f -> f.rewritePath("/eak/membership/(?<segment>.*)", "/${segment}")
                                .circuitBreaker(config -> config.setName("membershipCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/membership"))
                        )
                        .uri("lb://MEMBERSHIP-SERVICE")
                )
                .route(p -> p.
                        path("/eak/subscription/**")
                        .filters(f -> f.rewritePath("/eak/subscription/(?<segment>.*)", "/${segment}")
                                .circuitBreaker(config -> config.setName("subscriptionCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/subscription"))
//                                .retry(retryConfig -> retryConfig.setRetries(3)
//                                        .setMethods(HttpMethod.GET)
//                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true))
                       )
                        .uri("lb://SUBSCRIPTION-SERVICE")
                )
                .route(p -> p.
                        path("/eak/access/**")
                        .filters(f -> f.rewritePath("/eak/access/(?<segment>.*)", "/${segment}")
                                .circuitBreaker(config -> config.setName("accessCircuitBreaker")
                                     .setFallbackUri("forward:/fallback/access")))
                        .uri("lb://ACCESS-SERVICE")
                )
                .build();

    }


}
