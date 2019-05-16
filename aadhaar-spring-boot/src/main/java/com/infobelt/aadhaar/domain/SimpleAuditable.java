package com.infobelt.aadhaar.domain;

import java.time.ZonedDateTime;

/**
 * A simple interface which a {@link AbstractKeyed} can implement if
 * it wants to be updated by the {@link com.infobelt.aadhaar.service.AbstractEntityService}
 */
public interface SimpleAuditable {

    void setCreatedOn(ZonedDateTime createdOn);

    void setUpdatedOn(ZonedDateTime updatedOn);

    void setCreatedBy(String createdBy);

    void setUpdatedBy(String updatedBy);

}
