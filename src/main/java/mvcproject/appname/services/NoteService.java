package mvcproject.appname.services;

import mvcproject.appname.model.Note;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;

public interface NoteService {
  public Note saveNote(Note note);

  public Note findNoteByUserId(int id);

  public Page<Note> getAllPageNotes(Integer pageNum);


  public Note findNoteById(Long id);

  public void updateNote(Long id, Note note);

  public void deleteNoteById(Long id);
}
