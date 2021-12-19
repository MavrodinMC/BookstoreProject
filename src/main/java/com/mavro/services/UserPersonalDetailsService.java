package com.mavro.services;

import com.mavro.dto.UserPersonalDetailsDto;
import com.mavro.entities.AppUser;
import com.mavro.entities.UserPersonalDetails;
import com.mavro.exceptions.EmailNotFoundException;
import com.mavro.repositories.AppUserRepository;
import com.mavro.repositories.UserPersonalDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class UserPersonalDetailsService {

    private final UserPersonalDetailsRepository userPersonalDetailsRepository;
    private final AppUserRepository appUserRepository;

    public UserPersonalDetails getUserPersonalDetails(String email) {

        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(EmailNotFoundException::new);

        return userPersonalDetailsRepository.findById(appUser.getId()).get();
    }

    public void updateUserPersonalDetails(int id, UserPersonalDetailsDto userPersonalDetailsDto) {

        UserPersonalDetails userPersonalDetails = userPersonalDetailsRepository.getById(id);

        userPersonalDetails.setFavoriteAuthor(userPersonalDetailsDto.getFavoriteAuthor());
        userPersonalDetails.setFavoriteBook(userPersonalDetailsDto.getFavoriteBook());
        userPersonalDetails.setFavoriteQuote(userPersonalDetailsDto.getFavoriteQuote());
        userPersonalDetails.setAboutYourself(userPersonalDetailsDto.getAboutYourself());
        userPersonalDetails.setUpdatedAt(Instant.now());

        userPersonalDetailsRepository.save(userPersonalDetails);
    }

}
