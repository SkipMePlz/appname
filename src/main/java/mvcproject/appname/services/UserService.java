package mvcproject.appname.services;

import mvcproject.appname.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public User saveUser(User user);
    public User findByEmail(String email);

}
