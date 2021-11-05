package com.mavro.repositories;

import com.mavro.entities.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken, Integer> {

    Optional<ForgotPasswordToken> findByResetToken(String resetToken);

}
