package com.infobelt.aadhaar.example.domain;

import com.infobelt.aadhaar.domain.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Widget extends AbstractEntity {

    private String widgetName;

}
