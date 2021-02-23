package mvcproject.appname.controllers;

import mvcproject.appname.model.Note;
import mvcproject.appname.model.User;
import mvcproject.appname.services.NoteService;
import mvcproject.appname.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@Controller
public class MainController {
    private UserService userService;
    private NoteService noteService;
    @Autowired
    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }

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
        if(user.getName() == ""){
            bindingResult.rejectValue("name","name.error","Укажите Ваше имя");
            return "registration";
        }
        if(user.getSurname() == ""){
            bindingResult.rejectValue("surname","surname.error","Укажите Вашу фамилию");
            return "registration";
        }
        if(user.getPassword() == "" || user.getPassword().length() < 6){
            bindingResult.rejectValue("password","password.error","Пароль должен быть длиннее 6 символов");
            return "registration";
        }
        if(userService.findByEmail(user.getEmail()) != null){
            bindingResult.rejectValue("email","email.error","E-mail уже используется");
            return "registration";
        }
        userService.saveUser(user);
    return "redirect:/login";
    }
    @GetMapping("/adminHome")
    public String adminHomePage(Model model){
        model.addAttribute("note",new Note());
       return "adminHome";
    }
    @GetMapping("/news")
    public ModelAndView userHomePage(){
        ModelAndView modelAndView = new ModelAndView("news");
        List<Note> notes = noteService.getAllNotes();
        modelAndView.addObject("notes",notes);

        return modelAndView;
    }
    @PostMapping("/adminHome")
    public String createNote(@ModelAttribute("note") Note note,BindingResult bindingResult){
        if(note.getText()=="" || note == null){
            bindingResult.rejectValue("text","text.error","Нельзя отправить пустую запись");
            return "adminHome";
        }
        if(note.getTitle()=="" || note.getTitle() == null){
            bindingResult.rejectValue("title","title.error","Укажите заголовок");
            return "adminHome";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());
        note.setAuthor(user);
        noteService.saveNote(note);
        return "redirect:/news";
    }
    @GetMapping("/accessDenied")
    public String accessDeniedPage(){
        return "accessDenied";
    }

    @GetMapping("/adminHome/{id}/edit")
    public String noteEdit(@PathVariable("id") Long id,Model model){
        Note selectedNote = noteService.findById(id);
        model.addAttribute("note",selectedNote);
        return "note-edit";
    }
    @PostMapping("/adminHome/{id}/edit")
    public String noteUpdate(@ModelAttribute("note") Note note,@PathVariable("id") Long id, BindingResult bindingResult){
        if(note.getText()=="" || note == null){
            bindingResult.rejectValue("text","text.error","Нельзя отправить пустую запись");
            return "note-edit";
        }
        if(note.getTitle()=="" || note.getTitle() == null){
            bindingResult.rejectValue("title","title.error","Укажите заголовок");
            return "note-edit";
        }
        noteService.updateNote(id,note);
        return "redirect:/news";
    }
    @PostMapping("/adminHome/{id}")
    public String noteDeletePage(@PathVariable("id") Long id){
        noteService.deleteNoteById(id);
        return "redirect:/news";
    }
}
