package com.infobelt.aadhaar.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * The most basic unit of Aadhaar is a keyed object, with an id
 */
@Data
@MappedSuperclass
@EqualsAndHashCode(of = "id")
public abstract class AbstractKeyed {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

}
