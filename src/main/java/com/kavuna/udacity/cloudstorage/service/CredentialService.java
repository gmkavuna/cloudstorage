package com.kavuna.udacity.cloudstorage.service;


import com.kavuna.udacity.cloudstorage.mapper.CredentialMapper;
import com.kavuna.udacity.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }
    public int insertCredential(Credential credential){
        return credentialMapper.insert(credential);
    }

    public boolean deleteCredential(int credentialId){
        return  credentialMapper.deleteCredential(credentialId);
    }

    public boolean updateCredential(Credential credential){
        return credentialMapper.updateCredential(credential);
    }

    public List<Credential> getAllCredentials(){
        return credentialMapper.getAllCredentials();
    }
}