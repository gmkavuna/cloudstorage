package com.kavuna.udacity.cloudstorage.controller;

import com.kavuna.udacity.cloudstorage.model.Credential;
import com.kavuna.udacity.cloudstorage.service.CredentialService;
import com.kavuna.udacity.cloudstorage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

@Controller
public class CredentialController {

    private final CredentialService credentialService;
    private final UserService userService;

    @Autowired
    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;

    }

    @PostMapping("/addCredential")
    public String addCredential(Authentication authentication, Credential credential, Model model){

        try {
            String errorMessage = null;
            String successMessage = null;

            if (!authentication.isAuthenticated()){
                throw new Exception("Please make sure that you are logged in. ");
            }
            if (userService.getUser(authentication.getName()) == null) {
                throw new Exception("Your session might have expired. Please log back in.");
            }

            //update if credential object exists
            if (credential.getCredentialId() != null) {
                if (credentialService.updateCredential(credential)) {
                    model.addAttribute("successMessage", "Credential successfully updated!");
                }
            } else {
                //add credential's owner - logged in user
                int currentUserId = userService.getUser(authentication.getName()).getUserId();
                credential.setUserId(currentUserId);

                int rowsAdded = credentialService.insertCredential(credential);
                if (rowsAdded < 0) {
                    errorMessage = "We were unable to add your note!";
                    model.addAttribute("errorMessage", errorMessage);
                } else {
                    model.addAttribute("successMessage", "Credential successfully created!");
                }
            }
        }
        catch (Exception exception){
            model.addAttribute("errorMessage", exception.getMessage());
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
