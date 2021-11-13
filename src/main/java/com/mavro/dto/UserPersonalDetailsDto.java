package com.mavro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPersonalDetailsDto {

    private String favoriteAuthor;
    private String favoriteBook;
    private String favoriteQuote;
    private String aboutYourself;
    private Instant updatedAt;
}
