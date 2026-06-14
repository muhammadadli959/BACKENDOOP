// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/security/UserDetailsServiceImpl.java
package com.example.backend.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = userRepository.findByUsername(username);
        User user = opt.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new JwtUserDetails(user);
    }
}
