package mvcproject.appname.controllers;

import mvcproject.appname.model.Note;
import mvcproject.appname.model.User;
import mvcproject.appname.services.NoteService;
import mvcproject.appname.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @GetMapping("/login")
  public String loginPage() {
    return "login";
  }

  @GetMapping("/registration")
  public ModelAndView registrationPage() {
    ModelAndView modelAndView = new ModelAndView("registration");
    modelAndView.addObject("user", new User());
    return modelAndView;
  }

  @PostMapping("/registration")
  public String registration(@ModelAttribute("user") User user, BindingResult bindingResult) {
    // checking
    if (user.getName() == "") {
      bindingResult.rejectValue("name", "name.error", "Укажите Ваше имя");
      return "registration";
    }
    if (user.getSurname() == "") {
      bindingResult.rejectValue("surname", "surname.error", "Укажите Вашу фамилию");
      return "registration";
    }
    if (user.getPassword() == "" || user.getPassword().length() < 6) {
      bindingResult.rejectValue(
          "password", "password.error", "Пароль должен быть длиннее 6 символов");
      return "registration";
    }
    if (userService.findByEmail(user.getEmail()) != null) {
      bindingResult.rejectValue("email", "email.error", "E-mail уже используется");
      return "registration";
    }
    userService.saveUser(user);
    return "redirect:/login";
  }

  @GetMapping("/adminHome")
  public String adminHomePage(Model model) {
    model.addAttribute("note", new Note());
    return "adminHome";
  }

  @GetMapping({"/news","/"})
  public ModelAndView userHomePage(@RequestParam(required = false) Integer page) {
    if (page == null){
      page = 1;
    }
    ModelAndView modelAndView = new ModelAndView("news");
    Page<Note> pages = noteService.getAllPageNotes(page);
    List<Note> notes = pages.getContent();
    int totalPages = pages.getTotalPages();
    modelAndView.addObject("notes", notes);
    modelAndView.addObject("currentPage",page);
    modelAndView.addObject("totalPages",totalPages);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = userService.findByEmail(authentication.getName());
    modelAndView.addObject("user", user);

    return modelAndView;
  }

  @PostMapping("/adminHome")
  public String createNote(@ModelAttribute("note") Note note, BindingResult bindingResult) {
    if (note.getText() == "" || note == null) {
      bindingResult.rejectValue("text", "text.error", "Нельзя отправить пустую запись");
      return "adminHome";
    }
    if (note.getTitle() == "" || note.getTitle() == null) {
      bindingResult.rejectValue("title", "title.error", "Укажите заголовок");
      return "adminHome";
    }
    if (note.getText().length() > 2000) {
      bindingResult.rejectValue("text", "text.error", "Текст может содержать до 2000 символов");
      return "adminHome";
    }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = userService.findByEmail(authentication.getName());
    note.setAuthor(user);
    noteService.saveNote(note);
    return "redirect:/news";
  }

  @GetMapping("/accessDenied")
  public String accessDeniedPage() {
    return "accessDenied";
  }

  @GetMapping("/adminHome/{id}/edit")
  public String noteEdit(@PathVariable("id") Long id, Model model) {
    Note selectedNote = noteService.findNoteById(id);
    model.addAttribute("note", selectedNote);
    return "note-edit";
  }

  @PostMapping("/adminHome/{id}/edit")
  public String noteUpdate(
      @ModelAttribute("note") Note note, @PathVariable("id") Long id, BindingResult bindingResult) {
    if (note.getText() == "" || note == null) {
      bindingResult.rejectValue("text", "text.error", "Нельзя отправить пустую запись");
      return "note-edit";
    }
    if (note.getTitle() == "" || note.getTitle() == null) {
      bindingResult.rejectValue("title", "title.error", "Укажите заголовок");
      return "note-edit";
    }
    if (note.getTitle().length() >=255) {
      bindingResult.rejectValue("title", "title.error", "Слишком длинный заголовой");
      return "note-edit";
    }
    noteService.updateNote(id, note);
    return "redirect:/news";
  }

  @GetMapping("/adminHome/{id}")
  public String noteDeletePage(@PathVariable("id") Long id) {
    noteService.deleteNoteById(id);
    return "redirect:/news";
  }
  @GetMapping("/news/details/{id}")
  public String noteDetails(@PathVariable("id") Long id, Model model){
      model.addAttribute("note",noteService.findNoteById(id));
    return "detail";
  }
}
