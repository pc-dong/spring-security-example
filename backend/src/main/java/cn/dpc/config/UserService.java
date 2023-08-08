package cn.dpc.config;

import cn.dpc.domain.security.Role;
import cn.dpc.domain.security.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService implements ReactiveUserDetailsService {
    private final PasswordEncoder passwordEncoder;

    private Map<String, User> data;

    @PostConstruct
    public void init() {
        data = new HashMap<>();

        //username:passwowrd -> user:user
        data.put("user", new User("user", passwordEncoder.encode( "password"), true, Arrays.asList(Role.ROLE_USER)));

        //username:passwowrd -> admin:admin
        data.put("admin", new User("admin", passwordEncoder.encode( "password"), true, Arrays.asList(Role.ROLE_ADMIN)));
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.justOrEmpty(data.get(username))
                .map(user -> {
                    System.out.println(user);
                    return user;
                });
    }
}
