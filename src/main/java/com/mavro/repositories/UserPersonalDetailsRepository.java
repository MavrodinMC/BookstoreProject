package com.mavro.repositories;

import com.mavro.entities.UserPersonalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPersonalDetailsRepository extends JpaRepository<UserPersonalDetails, Integer> {

}
