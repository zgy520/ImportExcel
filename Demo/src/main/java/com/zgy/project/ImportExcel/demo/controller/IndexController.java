package com.zgy.project.ImportExcel.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/demo")
@Controller
public class IndexController {

    @RequestMapping(value = "index")
    public String index(){
        return "index";
    }
}
