package com.infobelt.aadhaar.example.services;

import com.infobelt.aadhaar.example.domain.Thingy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * An example of a custom auditor
 */
@Service
@Slf4j
public class CustomAuditor {

    public void auditLogMe(Thingy thingy) {
        log.info("I'm auditing " + thingy);
    }
}

