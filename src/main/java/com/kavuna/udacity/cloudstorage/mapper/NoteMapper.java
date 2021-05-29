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
            @Result(property = "userId", column = "salt"),

    })
    Note getNoteById(Integer noteId);

    @Insert("insert into notes (notetitle, notedescription) values (#{noteTitle}, #{noteDescription})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note note);

    @Delete("delete from notes where noteid =#{noteId}")
    public void deleteNote(Integer noteId);

    @Select("select * from notes")
    public List<Note> getAllNotes();
}