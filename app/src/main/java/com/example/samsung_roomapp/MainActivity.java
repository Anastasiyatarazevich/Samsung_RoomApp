package com.example.samsung_roomapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText edName, edAge;
    private Button btnSave, btnGet;

    private PersonDatabase personDB;
    private ExecutorService executorService;
    private Handler mainThreadHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edName = findViewById(R.id.editTextText);
        edAge = findViewById(R.id.editTextText2);

        btnSave = findViewById(R.id.button);
        btnGet = findViewById(R.id.button2);

        personDB = PersonDatabase.getInstance(getApplicationContext());
        executorService = Executors.newSingleThreadExecutor();
        mainThreadHandler = new Handler(Looper.getMainLooper());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edName.getText().toString();
                String age = edAge.getText().toString();

                if (name.isEmpty() || age.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Вы не ввели все данные",
                            Toast.LENGTH_SHORT).show();
                }
                Person person = new Person(name, age);
                MainActivity.this.addPersonInBackground(person);
            }
        });

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.getPersonListInBackground();
            }
        });

    }

    private void addPersonInBackground(Person person) {
        executorService.execute(() -> {
            personDB.getPersonDao().addPerson(person);
            mainThreadHandler.post(() ->
                    Toast.makeText(MainActivity.this, "Запись добавлена",
                            Toast.LENGTH_SHORT).show());
        });
    }


    private void getPersonListInBackground() {
        executorService.execute(() -> {
            List<Person> personList = personDB.getPersonDao().getAllPersons();
            mainThreadHandler.post(() -> {
                if (personList.isEmpty()) {
                    Toast.makeText(MainActivity.this, "База данных пуста",
                            Toast.LENGTH_SHORT).show();
                }

                StringBuilder stringBuilder = new StringBuilder();
                for (Person p : personList) {
                    stringBuilder.append(p.getName()).append(" ").append(p.getAge());
                }
                Toast.makeText(MainActivity.this, stringBuilder.toString(),
                        Toast.LENGTH_SHORT).show();

            });
        });
    }
}