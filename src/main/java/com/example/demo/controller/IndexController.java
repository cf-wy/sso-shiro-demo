package com.example.demo.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @GetMapping({"/index","/"})
    public String index(Model model){
        String username= SecurityUtils.getSubject().getPrincipal().toString();
        model.addAttribute("username",username);
        return "index";
    }

    @GetMapping("/s/hello")
    @ResponseBody
    public String  hello(){
        return "hello";
    }
}
