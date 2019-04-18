package com.infobelt.aadhaar.example.domain;

import com.infobelt.aadhaar.test.AbstractEntityTestBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class WidgetEntityTest extends AbstractEntityTestBase<Widget> {

    @Override
    public Widget getTestEntity() {
        Widget widget = new Widget();
        widget.setWidgetName("Widget 1");
        return widget;
    }

}
