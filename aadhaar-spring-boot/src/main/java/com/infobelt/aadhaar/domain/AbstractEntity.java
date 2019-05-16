package com.infobelt.aadhaar.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Base abstract implementation of a domain object that we will use a foundation
 * for all handling of these types of object.
 */
@Data
@MappedSuperclass
@EqualsAndHashCode(of = "uuid", callSuper = false)
public abstract class AbstractEntity extends AbstractKeyed implements Serializable, SimpleAuditable {

    @Column(name = "uuid")
    private String uuid = UUID.randomUUID().toString();

    @Column(name = "created_on")
    private ZonedDateTime createdOn;

    @Column(name = "updated_on")
    private ZonedDateTime updatedOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

}
