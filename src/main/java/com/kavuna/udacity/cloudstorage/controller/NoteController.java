package com.kavuna.udacity.cloudstorage.controller;


import com.kavuna.udacity.cloudstorage.model.Note;
import com.kavuna.udacity.cloudstorage.service.NoteService;
import com.kavuna.udacity.cloudstorage.service.UserService;
import com.kavuna.udacity.cloudstorage.storage.StorageException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/addNote")
    public String addNote(Authentication authentication, Note note, Model model) throws Exception {

        try {

            if (!authentication.isAuthenticated()) {
                throw new Exception("Please make sure you are logged in.");
            }
            if (userService.getUser(authentication.getName()) == null) {
                throw new Exception("Your session might have expired. Please log back in.");
            }
            String successMessage = null;
            String errorMessage = null;

            if (note.getNoteId() != 0) {
                if (noteService.updateNote(note)) {
                    successMessage = "Note successfully updated!";
                } else {
                    errorMessage = "Error updating note!";
                }
            } else {
                //update the note's owner
                note.setUserId(userService.getUser(authentication.getName()).getUserId());
                if (noteService.createNote(note) < 0) {
                    errorMessage = "We were unable to add your note!";
                } else {
                    successMessage = "Note successfully added!";
                }
            }
            model.addAttribute("successMessage", successMessage);
            model.addAttribute("errorMessage", errorMessage);

        } catch (Exception exception) {
            model.addAttribute("errorMessage", exception.getMessage());
        }
        return "result";
    }

    @GetMapping("/deleteNote")
    public String deleteNotePage(Model model, Note note, @RequestParam(value = "id", required = false) String noteId) throws InterruptedException, ExecutionException, IOException {
        String errorMessage = "Error while deleting note!";
        String successMessage = "Note successfully deleted!";

        if (noteId == null) {
            model.addAttribute("note", note);
        } else {
            if (noteService.deleteNote(Integer.parseInt(noteId))) {
                errorMessage = null;
            } else {
                successMessage = null;
            }
        }
        model.addAttribute("noteId", noteId);
        model.addAttribute("successMessage", successMessage);
        model.addAttribute("errorMessage", errorMessage);
        return "result";
    }
}