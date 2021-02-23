package mvcproject.appname.services;

import mvcproject.appname.model.Note;

import java.util.List;

public interface NoteService {
    public Note saveNote(Note note);
    public Note findNoteByUserId(int id);
    public List<Note> getAllNotes();
    public Note findById(Long id);
    public void updateNote(Long id, Note note);
    public void deleteNoteById(Long id);

}
