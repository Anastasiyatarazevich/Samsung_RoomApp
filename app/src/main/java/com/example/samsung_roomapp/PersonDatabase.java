package com.example.samsung_roomapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

@Database(entities = {Person.class}, version = 2)
public abstract class PersonDatabase extends RoomDatabase {
    private volatile static PersonDatabase instance;

    public abstract PersonDao getPersonDao();

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("alter table Person add column email text default ''");
        }
    };

    private static final RoomDatabase.Callback roomCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                }

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    Executors.newSingleThreadExecutor().execute(() -> {
                        instance.getPersonDao().addPerson(new Person("Мария", "25"));
                        instance.getPersonDao().addPerson(new Person("Илья", "1"));
                        instance.getPersonDao().addPerson(new Person("Федор", "83"));
                        instance.getPersonDao().addPerson(new Person("Илон", "56"));
                    });
                }
            };


    public static PersonDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (PersonDatabase.class) {
                instance = Room.databaseBuilder(context.getApplicationContext(), PersonDatabase.class,
                                "person_database")
                        .addCallback(roomCallback)
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
        }
        return instance;
    }

}
