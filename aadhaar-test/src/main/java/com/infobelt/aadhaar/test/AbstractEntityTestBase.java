package com.infobelt.aadhaar.test;

import com.infobelt.aadhaar.domain.AbstractEntity;
import com.infobelt.aadhaar.service.AbstractEntityService;
import lombok.Getter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractEntityTestBase<T extends AbstractEntity> {

    @Autowired
    @Getter
    AbstractEntityService<T> serviceLayer;

    @Test
    public void testRead() {
        serviceLayer.findAll();
    }

    @Test
    public void testSave() {
        serviceLayer.save(getTestEntity());
    }

    public abstract T getTestEntity();
}
