package com.kavuna.udacity.cloudstorage.mapper;

import com.kavuna.udacity.cloudstorage.model.File;
import com.kavuna.udacity.cloudstorage.model.Note;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileMapper {
    @Insert("insert into files (filename, userid) values (#{filename}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Select("select files.* from files, users where files.userid = users.userid and users.username =#{username}")
    List<File> getFilesByUsername(String Username);

    @Select("select files.filename from files, users where files.userid = users.userid and users.username =#{username}")
    List<String> getUserFileList(String Username);
}
