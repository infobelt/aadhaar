package com.infobelt.aadhaar.test;

import com.infobelt.aadhaar.domain.AbstractEntity;
import com.infobelt.aadhaar.service.AbstractEntityService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * A very basic test base that performs very basic tests on the JPA layer
 *
 * TODO should be moved over to the YAML approach
 *
 * @param <T>
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
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
