package com.infobelt.aadhaar.example.domain;

import com.infobelt.aadhaar.test.AbstractEntityTestBase;

public class WidgetEntityTest extends AbstractEntityTestBase<Widget> {

    @Override
    public Widget getTestEntity() {
        Widget widget = new Widget();
        widget.setWidgetName("Widget 1");
        return widget;
    }

}
