package com.example.samsung_roomapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Person")
public class Person {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "name")
    String name;
    @ColumnInfo(name = "age")
    String age;

    @ColumnInfo(name = "email")
    String email;

    public Person(){}
    public Person(String name, String age){
        this.age = age;
        this.name = name;
        this.id = 0;
    }

    public String getName(){
        return name;
    }
    public String getAge(){
        return age;
    }
}
