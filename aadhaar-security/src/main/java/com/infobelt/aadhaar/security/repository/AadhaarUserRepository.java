package com.infobelt.aadhaar.security.repository;

import com.infobelt.aadhaar.security.domain.AadhaarUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AadhaarUserRepository extends JpaRepository<AadhaarUser, Long> {

    Optional<AadhaarUser> findByEmail(String email);
    
}
