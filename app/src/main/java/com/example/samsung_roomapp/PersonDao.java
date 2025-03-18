package com.example.samsung_roomapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PersonDao {
    @Insert
    public void addPerson(Person person);

    @Update
    public void updatePerson(Person person);

    @Delete
    public void deletePerson(Person person);

    @Query("select * from person")
    public List<Person> getAllPersons();

    @Query("select * from person where id==:person_id")
    public List<Person> getAllPersons(int person_id);
}
