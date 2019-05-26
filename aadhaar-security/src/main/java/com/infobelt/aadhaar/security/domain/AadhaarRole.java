package com.infobelt.aadhaar.security.domain;

import com.infobelt.aadhaar.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class AadhaarRole extends AbstractEntity {

    private String name;
    
}
