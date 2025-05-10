package com.example.bet_api.service.impl;

import com.example.bet_api.model.User;
import com.example.bet_api.repository.UserRepository;
import com.example.bet_api.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(s);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user with name: " + s);
        }
        return new UserDetailsImpl(user);
    }
}
