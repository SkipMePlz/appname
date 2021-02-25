package mvcproject.appname.services;

import mvcproject.appname.model.Note;
import mvcproject.appname.repositories.NoteRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
  private static LocalDateTime today;
  private NoteRepository noteRepository;

  public NoteServiceImpl(NoteRepository noteRepository) {
    this.noteRepository = noteRepository;
  }

  @Override
  public Note saveNote(Note note) {
    today = LocalDateTime.now();
    note.setDate(today);
    return noteRepository.save(note);
  }

  @Override
  public Note findNoteByUserId(int id) {
    return null;
  }

  @Override
  public List<Note> getAllNotes() {
    return noteRepository.findAll(Sort.by(Sort.Direction.DESC, "date"));
  }

  @Override
  public Note findById(Long id) {
    return noteRepository.getOne(id);
  }

  @Override
  public void updateNote(Long id, Note note) {
    Note updatedNote = noteRepository.getOne(id);
    today = LocalDateTime.now();
    updatedNote.setText(note.getText());
    updatedNote.setTitle(note.getTitle());
    updatedNote.setDate(today);
    noteRepository.save(updatedNote);
  }

  @Override
  public void deleteNoteById(Long id) {
    noteRepository.deleteById(id);
  }
}
