package com.kavuna.udacity.cloudstorage.mapper;

import com.kavuna.udacity.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("select * from credentials")
    List<Credential> getAllCredentials();

    @Select("select * from credentials where userid =#{userId}")
    List<Credential> getCredentialsByUserId(int userId);

    @Select("select credentials.* from credentials, users where users.userid = credentials.userid and users.username =#{username}")
    List<Credential> getCredentialsByUsername(String username);

    @Insert("insert into credentials (url, username, key, password, userid) values (#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insert(Credential credential);

    @Select("select * from credentials where credentialid = #{credentialId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    Credential getCredential(int credentialId);

    @Update("update credentials set url=#{url}, username=#{username}, key=#{key}, password=#{password} where credentialid=#{credentialId}")
    boolean updateCredential(Credential credential);

    @Delete("delete from credentials where credentialid =#{credentialId}")
    boolean deleteCredential(Integer credentialId);
}