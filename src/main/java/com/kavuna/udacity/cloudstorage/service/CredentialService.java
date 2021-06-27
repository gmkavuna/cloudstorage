package com.kavuna.udacity.cloudstorage.service;


import com.kavuna.udacity.cloudstorage.mapper.CredentialMapper;
import com.kavuna.udacity.cloudstorage.model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    @Autowired
    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }
    public int insertCredential(Credential credential){

        credential.setKey(getEncryptionKey());
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), credential.getKey());
        credential.setPassword(encryptedPassword);
        return credentialMapper.insert(credential);
    }

    public boolean deleteCredential(int credentialId){
        return  credentialMapper.deleteCredential(credentialId);
    }

    public boolean updateCredential(Credential credential){
        credential.setKey(getEncryptionKey());
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), credential.getKey());
        credential.setPassword(encryptedPassword);
        return credentialMapper.updateCredential(credential);
    }

    public List<Credential> getAllCredentials(){
        List<Credential> decryptedCredentials = new ArrayList<Credential>();
        for (Credential credential: credentialMapper.getAllCredentials()) {
            credential.setUnencryptedPassword(encryptionService.decryptValue(credential.getPassword(), credential.getKey()));
            decryptedCredentials.add(credential);
        }
        return decryptedCredentials;
    }

    public Credential getCredential(int credentialId){
        return credentialMapper.getCredential(credentialId);
    }

    public String getEncryptionKey(){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        return encodedKey;

    }
    public List<Credential> getCredentialsByUserId(int userId){
        return credentialMapper.getCredentialsByUserId(userId);
    }
    public List<Credential> getCredentialsByUsername(String username){
        List<Credential> decryptedCredentials = new ArrayList<Credential>();
        for (Credential credential: credentialMapper.getCredentialsByUsername(username)) {
            credential.setUnencryptedPassword(encryptionService.decryptValue(credential.getPassword(), credential.getKey()));
            decryptedCredentials.add(credential);
        }
        return decryptedCredentials;
    }
}