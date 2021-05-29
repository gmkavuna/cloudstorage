package com.kavuna.udacity.cloudstorage.controller;

import com.kavuna.udacity.cloudstorage.model.Credential;
import com.kavuna.udacity.cloudstorage.model.Note;
import com.kavuna.udacity.cloudstorage.service.CredentialService;
import com.kavuna.udacity.cloudstorage.service.NoteService;
import com.kavuna.udacity.cloudstorage.service.UserService;
import org.apache.ibatis.jdbc.Null;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Controller
public class CredentialController {

    private final CredentialService credentialService;
    private final UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping("/addCredential")
    public String addCredential(Authentication authentication, Credential credential, Model model){
        String errorMessage = null;
        String successMessage = null;

        //update if credential object exists
        if (credential.getCredentialId() != null){
            if (credentialService.updateCredential(credential)== true){
                model.addAttribute("successMessage", "Credential successfully updated!");
            }
        }
        else{
            int rowsAdded = credentialService.insertCredential(credential);
            if (rowsAdded < 0) {
                errorMessage = "We were unable to add your note!";
                model.addAttribute("errorMessage", errorMessage);
            }
            else{
                model.addAttribute("successMessage", "Credential successfully created!");
            }
        }
        return "result";
    }
    @GetMapping("/deleteCredential")
    public String deleteCredentialPage(Model model, Credential credential, @RequestParam(value="id", required = false)String credentialId) throws InterruptedException, ExecutionException, IOException {

        String errorMessage = "Error while deleting credential!!";
        String successMessage = "Credential successfully deleted!!";

        if (credentialId == null){
            model.addAttribute("credential",  credential);
        }
        else{
            if (credentialService.deleteCredential(Integer.parseInt(credentialId))){
                errorMessage = null;
            }
            else{
                successMessage = null;
            }

        }
        model.addAttribute("credentialId",  credentialId);
        model.addAttribute("successMessage",  successMessage);
        model.addAttribute("errorMessage",  errorMessage);
        return "result";
    }}
