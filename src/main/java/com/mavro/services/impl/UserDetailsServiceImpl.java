package com.mavro.services.impl;

import com.mavro.entities.AppUser;
import com.mavro.exceptions.EmailNotFoundException;
import com.mavro.repositories.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<AppUser> userOptional = appUserRepository.findAppUserByEmail(email);
        AppUser user = userOptional.orElseThrow(EmailNotFoundException::new);

        return new MyAppUserDetails(user);
    }
}
