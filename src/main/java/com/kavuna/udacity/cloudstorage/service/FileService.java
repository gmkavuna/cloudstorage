package com.kavuna.udacity.cloudstorage.service;

import com.kavuna.udacity.cloudstorage.mapper.FileMapper;
import com.kavuna.udacity.cloudstorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    @Autowired
    FileService(FileMapper fileMapper){
        this.fileMapper = fileMapper;
    }
    List<File> getFilesByUsername(String username){
        return this.fileMapper.getFilesByUsername(username);
    }
    public int logFile(File file){
        return this.fileMapper.insert(file);
    }

    public List<String> getUserFileList(String username){
        return fileMapper.getUserFileList(username);
    }

}
