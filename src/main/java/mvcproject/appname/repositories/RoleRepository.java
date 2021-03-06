package mvcproject.appname.repositories;

import mvcproject.appname.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
  public Role getRoleById(int id);

  public Role findByRole(String role);
}
