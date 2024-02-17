package com.nayoon.api_gateway.config;

import com.nayoon.api_gateway.filter.AuthorizationHeaderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

  @Autowired
  private AuthorizationHeaderFilter authorizationHeaderFilter;

  @Bean
  public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
    return builder.routes()
        .route(r -> r.path("/user-service/auth/**")
            .filters(f -> f.rewritePath("/user-service/(?<segment>.*)", "/api/v1/${segment}"))
            .uri("lb://USER-SERVICE"))
        .route(r -> r.path("/user-service/signup")
            .filters(f -> f.rewritePath("/user-service/(?<segment>.*)", "/api/v1/${segment}"))
            .uri("lb://USER-SERVICE"))
        .route(r -> r.path("/user-service/users/**")
            .filters(f -> f
                .rewritePath("/user-service/(?<segment>.*)", "/api/v1/${segment}")
                .filter(authorizationHeaderFilter.apply(new AuthorizationHeaderFilter.Config())))
            .uri("lb://USER-SERVICE"))
        .route(r -> r.path("/product-service/**")
            .filters(f -> f
                .rewritePath("/product-service/(?<segment>.*)", "/api/v1/${segment}")
                .filter(authorizationHeaderFilter.apply(new AuthorizationHeaderFilter.Config())))
            .uri("lb://PRODUCT-SERVICE"))
        .route(r -> r.path("/purchase-service/**")
            .filters(f -> f
                .rewritePath("/purchase-service/(?<segment>.*)", "/api/v1/${segment}")
                .filter(authorizationHeaderFilter.apply(new AuthorizationHeaderFilter.Config())))
            .uri("lb://PURCHASE-SERVICE"))
        .route(r -> r.path("/payment-service/**")
            .filters(f -> f
                .rewritePath("/payment-service/(?<segment>.*)", "/api/v1/${segment}")
                .filter(authorizationHeaderFilter.apply(new AuthorizationHeaderFilter.Config())))
            .uri("lb://PAYMENT-SERVICE"))
        .build();
  }

}
