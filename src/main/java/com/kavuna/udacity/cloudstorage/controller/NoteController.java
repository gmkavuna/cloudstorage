package com.kavuna.udacity.cloudstorage.controller;


import com.kavuna.udacity.cloudstorage.model.Note;
import com.kavuna.udacity.cloudstorage.service.NoteService;
import com.kavuna.udacity.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Controller
public class NoteController {

    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/addNote")
    public String addNote(Authentication authentication, Note note, Model model){
        String errorMessage = null;


        int rowsAdded = noteService.createNote(note);
        if (rowsAdded < 0) {
            errorMessage = "We were unable to add your note!";
            model.addAttribute("errorMessage", errorMessage);
        }
        else{
            model.addAttribute("addNoteSuccess", true);

        }
        return "result";
    }
    @GetMapping("/deleteNote")
    public String deleteNotePage(Model model, Note note, @RequestParam(value="id", required = false)String noteId) throws InterruptedException, ExecutionException, IOException {
        if (noteId == null){
            model.addAttribute("note",  note);
        }
        else{

            noteService.deleteNote(Integer.parseInt(noteId));
        }
        model.addAttribute("noteId",  noteId);
        return "result";
    }



}