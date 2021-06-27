package com.kavuna.udacity.cloudstorage.controller;


import com.kavuna.udacity.cloudstorage.model.Credential;
import com.kavuna.udacity.cloudstorage.model.Note;
import com.kavuna.udacity.cloudstorage.service.AuthenticationService;
import com.kavuna.udacity.cloudstorage.service.CredentialService;
import com.kavuna.udacity.cloudstorage.service.FileService;
import com.kavuna.udacity.cloudstorage.service.NoteService;
import com.kavuna.udacity.cloudstorage.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private NoteService noteService;
    private CredentialService credentialService;
    private final StorageService storageService;
    private final FileService fileService;

    @Autowired
    public HomeController(NoteService noteService, CredentialService credentialService, StorageService storageService, FileService fileService) {
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.storageService = storageService;
        this.fileService = fileService;
    }

    @RequestMapping("/home")
    public String getHomePage(Model model, AuthenticationService authenticationService, Authentication authentication, Note note, Credential credential, @RequestParam(value = "noteid", required = false) String noteid) throws InterruptedException, ExecutionException, IOException {

        model.addAttribute("authenticationService", authenticationService);
        model.addAttribute("note", note); //pojo to hold add note form data
        model.addAttribute("credential", credential); //pojo to hold add credential form data


        if (noteService.getNotesByUsername(authentication.getName()).size() > 0) {
            model.addAttribute("notes", this.noteService.getNotesByUsername(authentication.getName()));
        } else {
            model.addAttribute("notes", null);
        }

        if (credentialService.getCredentialsByUsername(authentication.getName()).size() > 0) {
            model.addAttribute("credentials", this.credentialService.getCredentialsByUsername(authentication.getName()));
        } else {
            model.addAttribute("credentials", null);
        }
        //get all files in the system
        List<String> allFiles = storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList());

        //filter out files not owned by the user
        List<String> userFiles = new ArrayList<>();

        for (String filename : allFiles) {
            String[] filePathParts = URLDecoder.decode(filename, "UTF-8").split("/files/");
            if (fileService.getUserFileList(authentication.getName()).contains(filePathParts[1])) {
                userFiles.add(filename);
            }
        }
        model.addAttribute("files", userFiles);
        return "home";
    }
}