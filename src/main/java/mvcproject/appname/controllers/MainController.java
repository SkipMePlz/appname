package mvcproject.appname.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping({"/","/home"})
    public String homePage(){
        return "home";
    }

    @GetMapping("/hello")
    public String helloPage(){
        return "hello";
    }
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
}
