package com.infobelt.aadhaar.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.infobelt.aadhaar.domain.AbstractEntity;
import com.infobelt.aadhaar.service.AbstractEntityService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * A standard approach to integration testing the web layer based on the {@link com.infobelt.aadhaar.web.AbstractEntityResource}
 *
 * @param <T> The object that we will test
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public abstract class AbstractYamlResourceTestBase<T extends AbstractEntity> {

    private ObjectMapper om = new ObjectMapper(new YAMLFactory());

    @Autowired
    private MockMvc mvc;

    @Autowired
    @Getter
    AbstractEntityService<T> serviceLayer;

    private Long testId;

    private static String defaultYamlType = "default";

    /**
     * Implementation must provide the class that matches the generic
     *
     * @return class to match the generic
     */
    public abstract Class<T> getEntityClass();

    /**
     * The base URL that we will use to test the interactions
     *
     * @return The base URL
     */
    public abstract String getBaseUrl();

    /**
     * Before the tests run lets create an businessLine to test with
     */
    @Before
    public void setup() throws IOException {
            T instance = getInstance(defaultYamlType);
            testId = serviceLayer.save(instance).getId();
    }

    @After
    public void teardown(){
        if (testId != null){
            serviceLayer.delete(testId);
        }
    }

    protected T getInstance(String type) throws IOException {
        InputStream requestedType = getClass().getClassLoader().getResourceAsStream(getEntityClass().getSimpleName() + "-" + type + ".yaml");
        if(requestedType == null){
            requestedType = getClass().getClassLoader().getResourceAsStream(getEntityClass().getSimpleName()  + "-" + defaultYamlType + ".yaml");
        }
        return om.readValue(requestedType, getEntityClass());
    }

    private Map<String, String> getMap(String type) throws IOException {
        return om.readValue(getClass().getClassLoader().getResourceAsStream(getEntityClass().getSimpleName() + "-" + type + ".yaml"), new TypeReference<Map<String, String>>() {
        });

    }

    /**
     * Get a page of object and make sure we have the object we are testing with
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "admin")
    public void getAll()
            throws Exception {
        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get(getBaseUrl())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[*].id").value(hasItem(testId.intValue())));

        getMap(defaultYamlType).forEach((k, v) -> {
            try {
                result.andExpect(jsonPath("$.content[*]." + k).value(hasItem(v)));
            } catch (Exception e) {
                throw new RuntimeException("Unable to perform test", e);
            }
        });
    }

    /**
     * Get the test object by id
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "admin")
    public void getById() throws Exception {
        ResultActions result = mvc.perform(MockMvcRequestBuilders.get(getBaseUrl() + testId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        getMap(defaultYamlType).forEach((k, v) -> {
            try {
                result.andExpect(jsonPath("$." + k).value(v));
            } catch (Exception e) {
                throw new RuntimeException("Unable to perform test", e);
            }
        });
    }

    /**
     * Update the test object and then revert it (so as not to impact other tests)
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "admin")
    public void update() throws Exception {
        T update = getInstance("update");

        update.setId(testId);
        ResultActions updateResult = mvc.perform(MockMvcRequestBuilders.put(getBaseUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(serialize(update)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testId));

        getMap("update").forEach((k, v) -> {
            try {
                updateResult.andExpect(jsonPath("$." + k).value(v));
            } catch (Exception e) {
                throw new RuntimeException("Unable to perform test", e);
            }
        });

        T revert = getInstance(defaultYamlType);
        revert.setId(testId);

        ResultActions revertResult = mvc.perform(MockMvcRequestBuilders.put(getBaseUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(serialize(revert)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testId));

        getMap(defaultYamlType).forEach((k, v) -> {
            try {
                revertResult.andExpect(jsonPath("$." + k).value(v));
            } catch (Exception e) {
                throw new RuntimeException("Unable to perform test", e);
            }
        });

    }

    // Used internally to serialize the object to JSON
    private String serialize(T businessLine) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(businessLine);
    }

    /**
     * Test that we can delete the object
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "admin")
    public void delete() throws Exception {
        T businessLine = getInstance("delete");
        Long deleteId = serviceLayer.save(businessLine).getId();

        mvc.perform(MockMvcRequestBuilders.delete(getBaseUrl() + "{id}", deleteId))
                .andExpect(status().isOk());
    }
}
