package com.kavuna.udacity.cloudstorage.mapper;

import com.kavuna.udacity.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE username = #{username}")
    @Results({
            @Result(property = "userId", column = "userid"),
            @Result(property = "username", column = "username"),
            @Result(property = "salt", column = "salt"),
            @Result(property = "password", column = "password"),
            @Result(property = "firstName", column = "firstname"),
            @Result(property = "lastName", column = "lastname"),
    })
    User getUser(String username);

    @Insert("INSERT INTO users (username, salt, password, firstname, lastname) VALUES(#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(User user);

    @Select("Select * from users")
    public List<User> getAllUsers();
}
