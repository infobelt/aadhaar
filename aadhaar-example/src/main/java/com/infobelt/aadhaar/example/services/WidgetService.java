package com.infobelt.aadhaar.example.services;

import com.infobelt.aadhaar.example.domain.Widget;
import com.infobelt.aadhaar.service.AbstractEntityService;
import org.springframework.stereotype.Service;

@Service
public class WidgetService extends AbstractEntityService<Widget> {

    @Override
    protected Class<Widget> getEntityClass() {
        return Widget.class;
    }

}
