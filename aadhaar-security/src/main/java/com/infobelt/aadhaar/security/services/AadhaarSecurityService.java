package com.infobelt.aadhaar.security.services;

import com.infobelt.aadhaar.security.repository.AadhaarUserRepository;
import com.infobelt.aadhaar.service.SecurityAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class  AadhaarSecurityService implements SecurityAdvisor {

    @Autowired
    AadhaarUserRepository userRepository;

    @Override
    public boolean isPermitted(String resource, String action) {
        return true;
    }

    @Override
    public boolean isPermitted(String resource, String action, Long id) {
        return true;
    }

}
