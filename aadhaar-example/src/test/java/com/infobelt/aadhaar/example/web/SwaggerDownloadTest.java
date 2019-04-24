package com.infobelt.aadhaar.example.web;

import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class SwaggerDownloadTest {

    @LocalServerPort
    int randomServerPort;

    @Test
    public void getSwagger() throws MalformedURLException {

        Path outputFile = Paths.get("docs/swagger");

        Swagger2MarkupConverter.from(new URL("http://localhost:" + randomServerPort + "/v2/api-docs"))
                .withConfig(new Swagger2MarkupConfigBuilder()
                        .withMarkupLanguage(MarkupLanguage.MARKDOWN).build())
                .build()
                .toFile(outputFile);
    }
}
