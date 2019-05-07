package com.infobelt.aadhaar.example.domain;

import com.infobelt.aadhaar.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

// Note don't use @Data since our abstract entity implements hashCode and equals and we don't want to
// override it
@Getter
@Setter
@Entity
public class Thingy extends AbstractEntity {

    private String name;

}
