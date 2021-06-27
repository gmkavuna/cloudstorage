package com.kavuna.udacity.cloudstorage.service;

import com.kavuna.udacity.cloudstorage.mapper.NoteMapper;
import com.kavuna.udacity.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    @Autowired
    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int createNote(Note note) {
        return noteMapper.insert(note);
    }

    public Note getNoteById(int noteId){
        return noteMapper.getNoteById(noteId);
    }

    public boolean updateNote(Note note){
        return noteMapper.updateNote(note);
    }

    public boolean deleteNote(int noteId){
        return noteMapper.deleteNote(noteId);
    }
    public  List<Note> getAllNotes(){
        return noteMapper.getAllNotes();
    }

    public List<Note> getNotesByUsername (String username){
        return noteMapper.getNotesByUsername(username);
    }
    public List<Note> getNotesByUserId (int userId){
        return noteMapper.getNotesByUserId(userId);
    }

}