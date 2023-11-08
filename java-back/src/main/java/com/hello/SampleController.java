package com.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
@EnableAutoConfiguration
public class SampleController {

    @RequestMapping(value = "/hello")
    @ResponseBody
    String helloCourseGet() {
        SampleModel model = new SampleModel("Sample data set: ", 342, new ArrayList<>(){{
            add("attr1");
            add("attr2");
        }});
        return model.getName() + " " + model.getAmount() + " " + model.getAttributes();
    }

    public static void main(String[] args) {
        SpringApplication.run(SampleController.class, args);
    }
}
