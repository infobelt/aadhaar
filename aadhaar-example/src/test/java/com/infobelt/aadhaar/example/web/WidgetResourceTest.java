package com.infobelt.aadhaar.example.web;

import com.infobelt.aadhaar.example.domain.Widget;
import com.infobelt.aadhaar.test.AbstractYamlResourceTestBase;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class WidgetResourceTest extends AbstractYamlResourceTestBase {

    @Override
    public Class<Widget> getEntityClass() {
        return Widget.class;
    }

    @Override
    public String getBaseUrl() {
        return "/api/widgets/";
    }

}
