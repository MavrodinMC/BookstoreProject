package com.mavro.services;

import com.mavro.dto.RegistrationRequest;
import com.mavro.entities.AppUser;
import com.mavro.exceptions.InvalidEmailException;
import com.mavro.repositories.AppUserRepository;
import com.mavro.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@Service
public class AuthService {

         private final EmailValidatorService emailValidatorService;
         private final AppUserRepository appUserRepository;
         private final RoleRepository roleRepository;
         private final PasswordEncoder passwordEncoder;


    public void registerUser(RegistrationRequest registrationRequest) {

        AppUser user = new AppUser();

        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRoles(Set.of(roleRepository.findRoleByName("USER")));
        user.setCreatedAt(LocalDateTime.now());
        user.setLocked(false);
        user.setEnabled(false);

        boolean isEmailValid = emailValidatorService.test(registrationRequest.getEmail());

        if (!isEmailValid) {
            throw new InvalidEmailException();
        }

        appUserRepository.save(user);
    }
}
