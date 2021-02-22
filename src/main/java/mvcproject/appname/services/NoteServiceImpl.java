package mvcproject.appname.services;

import mvcproject.appname.model.Note;
import mvcproject.appname.repositories.NoteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
@Service
public class NoteServiceImpl implements NoteService {
    private NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public Note saveNote(Note note) {
        LocalDate today = LocalDate.now();
        today.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
        note.setDate(today);
        return noteRepository.save(note);
    }

    @Override
    public Note findNoteByUserId(int id) {
        return null;
    }

    @Override
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }
}
