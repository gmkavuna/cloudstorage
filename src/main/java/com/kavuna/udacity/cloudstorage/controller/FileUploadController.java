package com.kavuna.udacity.cloudstorage.controller;


import com.kavuna.udacity.cloudstorage.model.File;
import com.kavuna.udacity.cloudstorage.service.FileService;
import com.kavuna.udacity.cloudstorage.service.UserService;
import com.kavuna.udacity.cloudstorage.storage.StorageFileNotFoundException;
import com.kavuna.udacity.cloudstorage.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

@Controller
public class FileUploadController {

    private final StorageService storageService;
    private final UserService userService;
    private final FileService fileService;

    @Autowired
    public FileUploadController(StorageService storageService, UserService userService, FileService fileService) {
        this.storageService = storageService;
        this.userService = userService;
        this.fileService = fileService;
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {

        Resource file = storageService.loadAsResource(filename);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/fileUpload")
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile file,
                                   RedirectAttributes redirectAttributes, Model model, Authentication authentication) {

        try {
            if (userService.getUser(authentication.getName()) == null) {
                throw new Exception("Your session might have expired. Please log back in.");
            }
            //uploads and registers file in the database along with the owner
            String uploadedFileName = storageService.store(file);
            File fileLog = new File();
            fileLog.setFilename(uploadedFileName);
            fileLog.setUserId(userService.getUser(authentication.getName()).getUserId());
            fileService.logFile(fileLog);
            model.addAttribute("successMessage",
                    "You successfully uploaded " + file.getOriginalFilename() + "!");
        } catch (Exception exception) {
            model.addAttribute("errorMessage", exception.getMessage());
        }
        return "result";
    }

    @GetMapping("/deleteFile")
    public String deleteFile(Model model, @RequestParam(value = "id", required = false) String filePath) throws InterruptedException, ExecutionException, IOException {

        String[] filePathParts = URLDecoder.decode(filePath, "UTF-8").split("/files/");
        Path path = storageService.load(filePathParts[1]);
        model.addAttribute("successMessage",
                "File " + path.getFileName() + " was successfully deleted!");
        Files.delete(path.normalize().toAbsolutePath());
        return "result";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
