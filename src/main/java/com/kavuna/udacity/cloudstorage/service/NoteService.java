package com.kavuna.udacity.cloudstorage.service;

import com.kavuna.udacity.cloudstorage.mapper.NoteMapper;
import com.kavuna.udacity.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int createNote(Note note) {
        return noteMapper.insert(note);
    }

    public  List<Note> getAllNotes(){
        return noteMapper.getAllNotes();
    }
}