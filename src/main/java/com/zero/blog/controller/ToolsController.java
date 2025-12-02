package com.zero.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tools/")
public class ToolsController {

    @GetMapping("/list")
    public String tools(Model model){
        return "/tools/tool";
    }

    @GetMapping("bmi")
    public String bmi(){
        return "/tools/bmi";
    }
}
