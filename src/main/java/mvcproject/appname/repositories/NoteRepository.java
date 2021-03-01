package mvcproject.appname.repositories;

import java.util.List;
import mvcproject.appname.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NoteRepository extends JpaRepository<Note, Long> {

@Query(value ="SELECT id, date, substring(text, 1, 150) as text, title,author_user_id from notes",
nativeQuery = true)
  @Override
  Page<Note> findAll(Pageable pageable);
}


