package com.infobelt.aadhaar.security.services;

import com.infobelt.aadhaar.security.domain.AadhaarUser;
import com.infobelt.aadhaar.security.repository.AadhaarUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPrincipalService implements UserDetailsService {

    @Autowired
    private AadhaarUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AadhaarUser> user = userRepository.findByEmail(email);
        user.orElseThrow(() -> new UsernameNotFoundException(email + " not found"));
        return new UserPrincipal(user.get());
    }
}

