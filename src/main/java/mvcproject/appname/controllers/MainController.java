package mvcproject.appname.controllers;

import mvcproject.appname.model.User;
import mvcproject.appname.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

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
    @GetMapping("/registration")
    public ModelAndView registrationPage(){
        ModelAndView modelAndView = new ModelAndView("registration");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }
    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") User user, BindingResult bindingResult){
        //checking
        if(userService.findByEmail(user.getEmail()) != null){
            bindingResult.rejectValue("email","email.error","E-mail is already used.");
            return "registration";
        }
        if(user.getPassword() == null || user.getPassword().length() < 6){
            bindingResult.rejectValue("password","password.error","Password should be 6 signs or more");
            return "registration";
        }
        if(user.getName() == null){
            bindingResult.rejectValue("name","name.error","You should write down your name");
            return "registration";
        }
        if(user.getSurname() == null){
            bindingResult.rejectValue("surname","surname.error","You should write down your surname");
            return "registration";
        }



        userService.saveUser(user);
    return "userHome";
    }
}
