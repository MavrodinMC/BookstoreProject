package com.mavro.services.adminOnlyServices;

import com.mavro.entities.AppUser;
import com.mavro.repositories.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public List<AppUser> getAllUsers() {

        return appUserRepository.findAll();
    }
}
