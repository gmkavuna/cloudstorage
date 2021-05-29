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
    Note getNoteById(int noteId);

    @Insert("insert into notes (notetitle, notedescription) values (#{noteTitle}, #{noteDescription})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note note);

    @Update("update notes set notetitle =#{noteTitle}, notedescription=#{noteDescription} where noteid=#{noteId}")
    public boolean updateNote(Note note);

    @Delete("delete from notes where noteid =#{noteId}")
    public boolean deleteNote(int noteId);

    @Select("select * from notes")
    public List<Note> getAllNotes();
}