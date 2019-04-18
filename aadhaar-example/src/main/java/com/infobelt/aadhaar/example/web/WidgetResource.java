package com.infobelt.aadhaar.example.web;


import com.infobelt.aadhaar.example.domain.Widget;
import com.infobelt.aadhaar.web.AbstractEntityResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/widgets")
public class WidgetResource extends AbstractEntityResource<Widget> {
}
