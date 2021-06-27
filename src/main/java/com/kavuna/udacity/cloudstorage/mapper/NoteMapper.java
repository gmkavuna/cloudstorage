package com.kavuna.udacity.cloudstorage.mapper;

import com.kavuna.udacity.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("select * from notes where noteid = #{noteid}")
    @Results({
            @Result(property = "noteId", column = "noteid"),
            @Result(property = "noteTitle", column = "notetitle"),
            @Result(property = "noteDescription", column = "username"),
            @Result(property = "userId", column = "userid"),

    })
    Note getNoteById(int noteId);

    @Insert("insert into notes (notetitle, notedescription, userid) values (#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note note);

    @Update("update notes set notetitle =#{noteTitle}, notedescription=#{noteDescription} where noteid=#{noteId}")
    boolean updateNote(Note note);

    @Delete("delete from notes where noteid =#{noteId}")
    boolean deleteNote(int noteId);

    @Select("select * from notes")
    public List<Note> getAllNotes();

    @Select("select * from notes where userid =#{userId}")
    List<Note> getNotesByUserId(int userId);

    @Select("select notes.* from notes, users where notes.userid = users.userid and users.username =#{username}")
    List<Note> getNotesByUsername(String username);
}