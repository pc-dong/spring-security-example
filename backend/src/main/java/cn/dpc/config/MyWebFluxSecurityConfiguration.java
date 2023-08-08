package cn.dpc.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import reactor.core.publisher.Mono;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@EnableMethodSecurity
public class MyWebFluxSecurityConfiguration {

    private final ReactiveAuthenticationManager authenticationManager;
    private final ServerSecurityContextRepository securityContextRepository;


    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.exceptionHandling(exceptionHandlingSpec ->
                exceptionHandlingSpec.authenticationEntryPoint((swe, e) ->
                                Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                        .accessDeniedHandler((swe, e) ->
                                Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
                        ));
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        http.authenticationManager(authenticationManager);
        http.formLogin(ServerHttpSecurity.FormLoginSpec::disable);
        http.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable);
        http.securityContextRepository(securityContextRepository);

        http.authorizeExchange((exchange) -> {
            exchange.matchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();
            exchange.pathMatchers("/").authenticated();
            exchange.pathMatchers("/login").permitAll();
//            exchange.pathMatchers(HttpMethod.OPTIONS).permitAll();

        });
        return http.build();
    }
}
