package com.kavuna.udacity.cloudstorage.controller;


import com.kavuna.udacity.cloudstorage.model.Credential;
import com.kavuna.udacity.cloudstorage.model.Note;
import com.kavuna.udacity.cloudstorage.service.AuthenticationService;
import com.kavuna.udacity.cloudstorage.service.CredentialService;
import com.kavuna.udacity.cloudstorage.service.NoteService;
import com.kavuna.udacity.cloudstorage.storage.StorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private NoteService noteService;
    private CredentialService credentialService;
    private final StorageService storageService;

    public HomeController(NoteService noteService, CredentialService credentialService, StorageService storageService){
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.storageService = storageService;
    }

    @RequestMapping("/home")
    public String getHomePage(Model model, AuthenticationService authenticationService, Note note, Credential credential, @RequestParam(value="noteid", required = false)String noteid) throws InterruptedException, ExecutionException, IOException {

        model.addAttribute("authenticationService", authenticationService);
        model.addAttribute("note", note); //pojo to hold add note form data
        model.addAttribute("credential", credential); //pojo to hold add note form data


        if (noteService.getAllNotes().size() > 0){
            model.addAttribute("notes", this.noteService.getAllNotes());
        }
        else{
            model.addAttribute("notes", null);
        }

        if (credentialService.getAllCredentials().size() > 0){
            model.addAttribute("credentials", this.credentialService.getAllCredentials());
        }
        else{
            model.addAttribute("credentials", null);
        }
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "home";
    }
}