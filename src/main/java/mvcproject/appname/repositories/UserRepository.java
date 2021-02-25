package mvcproject.appname.repositories;

import mvcproject.appname.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
  public User findOneByEmail(String email);
}
