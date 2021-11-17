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

    public UserPersonalDetails saveUserPersonalDetails(String email, UserPersonalDetailsDto userPersonalDetailsDto) {

        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(EmailNotFoundException::new);

        UserPersonalDetails userPersonalDetails = new UserPersonalDetails();
        userPersonalDetails.setFavoriteAuthor(userPersonalDetailsDto.getFavoriteAuthor());
        userPersonalDetails.setFavoriteBook(userPersonalDetailsDto.getFavoriteBook());
        userPersonalDetails.setFavoriteQuote(userPersonalDetailsDto.getFavoriteQuote());
        userPersonalDetails.setAboutYourself(userPersonalDetailsDto.getAboutYourself());
        userPersonalDetails.setUpdatedAt(userPersonalDetailsDto.getUpdatedAt());
        userPersonalDetails.setAppUser(appUser);

        return userPersonalDetailsRepository.save(userPersonalDetails);
    }

    public UserPersonalDetails getUserPersonalDetails(int id) {

        return userPersonalDetailsRepository.findById(id).get();
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
