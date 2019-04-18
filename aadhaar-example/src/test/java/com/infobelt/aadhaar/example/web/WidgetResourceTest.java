package com.infobelt.aadhaar.example.web;

import com.infobelt.aadhaar.example.domain.Widget;
import com.infobelt.aadhaar.test.AbstractYamlResourceTestBase;

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
