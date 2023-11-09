package com.hello.controllers;

import com.hello.models.SampleModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
public class SampleController {

    @RequestMapping("/hello")
    @ResponseBody
    String helloCourseGet() {
        SampleModel model = new SampleModel("Sample data set: ", 342, new ArrayList<>(){{
            add("attr1");
            add("attr2");
        }});
        return model.getName() + " " + model.getAmount() + " " + model.getAttributes();
    }
}
