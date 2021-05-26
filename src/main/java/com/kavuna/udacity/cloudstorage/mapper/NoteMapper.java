package com.kavuna.udacity.cloudstorage.mapper;

import com.kavuna.udacity.cloudstorage.model.Note;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("select * from notes where noteid = #{noteid}")
    Note getNoteById(Integer noteId);

    @Insert("insert into notes (notetitle, notedescription) values (#{noteTitle}, #{noteDescription})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note note);

    @Select("select * from notes")
    public List<Note> getAllNotes();
}