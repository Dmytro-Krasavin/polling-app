package com.example.polls.security.service.impl;

import com.example.polls.exception.ForbiddenException;
import com.example.polls.model.User;
import com.example.polls.repository.UserRepository;
import com.example.polls.security.model.UserPrincipal;
import com.example.polls.security.service.CustomUserDetailsService;
import com.example.polls.security.service.LoginAttemptService;
import com.example.polls.util.IpAddressExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserRepository userRepository;

    private final LoginAttemptService loginAttemptService;

    private final IpAddressExtractor ipAddressExtractor;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        checkClientIP();
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail));
        return UserPrincipal.create(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserById(Long id) {
        checkClientIP();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));
        return UserPrincipal.create(user);
    }

    private void checkClientIP() {
        String ip = ipAddressExtractor.getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new ForbiddenException("Client IP address is blocked");
        }
    }
}
