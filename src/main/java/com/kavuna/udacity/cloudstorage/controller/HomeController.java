package com.kavuna.udacity.cloudstorage.controller;


import com.kavuna.udacity.cloudstorage.model.Note;
import com.kavuna.udacity.cloudstorage.service.AuthenticationService;
import com.kavuna.udacity.cloudstorage.service.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Controller
public class HomeController {


    private NoteService noteService;

    public HomeController(NoteService noteService){
        this.noteService = noteService;

    }

    @RequestMapping("/home")
    public String getHomePage(Model model, AuthenticationService authenticationService, Note note, @RequestParam(value="noteid", required = false)String noteid) throws InterruptedException, ExecutionException, IOException {

        model.addAttribute("authenticationService", authenticationService);
        model.addAttribute("note", note); //pojo to hold add note form data


        if (noteService.getAllNotes().size() > 0){
            model.addAttribute("notes", this.noteService.getAllNotes());
        }
        else{
            model.addAttribute("notes", null);
        }


        return "home";
    }
}