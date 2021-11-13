package com.mavro.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.Instant;

@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_personal_details")
public class UserPersonalDetails {

    @Id
    private Integer id;

    @Column(name = "favorite_author")
    private String favoriteAuthor;

    @Column(name = "favorite_book")
    private String favoriteBook;

    @Column(name = "favorite_quote")
    private String favoriteQuote;

    @Column(name = "about_yourself")
    private String aboutYourself;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JsonIgnore
    private AppUser appUser;

    public UserPersonalDetails(String favoriteAuthor, String favoriteBook, String favoriteQuote, String aboutYourself, Instant updatedAt) {
        this.favoriteAuthor = favoriteAuthor;
        this.favoriteBook = favoriteBook;
        this.favoriteQuote = favoriteQuote;
        this.aboutYourself = aboutYourself;
        this.updatedAt = updatedAt;
    }
}
