package com.ugive.security.provider;

import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.models.User;
import com.ugive.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDetailsProvider implements UserDetailsService {

    private static final Logger log = Logger.getLogger(UserDetailsProvider.class);

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByAuthenticationInfoEmail(email).orElseThrow(() ->
                new EntityNotFoundException("User not exists with this e-mail"));

//        if (user.isPresent()) {
//            User user = user.get();
//            AuthenticationInfo authenticationInfo = user.getAuthenticationInfo();
//            List<Role> roles = user.getRoles();

//            log.info("Fetching user by e-mail: " + email);
//            List<String> rolesNames = new ArrayList<>();
//            for(Role role : roles) {
//                rolesNames.add(role.getRoleName());
//                log.info("User role: " + role.getRoleName());
//            }

        Set<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                email,
                user.getAuthenticationInfo().getPassword(),
                authorities
        );
    }
}
