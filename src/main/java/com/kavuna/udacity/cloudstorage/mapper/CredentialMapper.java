package com.kavuna.udacity.cloudstorage.mapper;

import com.kavuna.udacity.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("select * from credentials")
    public List<Credential> getAllCredentials();

    @Insert("insert into credentials (url, username, key, password) values (#{url}, #{username}, #{key}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insert(Credential credential);

    @Select("select * from credentials where credentialid = #{credentialId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    public Credential getCredential(int  credentialId);

    @Update("update credentials set url=#{url}, username=#{username}, key=#{key}, password=#{password} where credentialid=#{credentialId}")
    public boolean updateCredential(Credential credential);


    @Delete("delete from credentials where credentialid =#{credentialId}")
    public boolean deleteCredential(Integer credentialId);
}