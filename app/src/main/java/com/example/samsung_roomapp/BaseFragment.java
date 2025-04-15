package com.example.samsung_roomapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BaseFragment extends Fragment {

    private EditText edName, edAge;
    private Button btnSave, btnGet;

    private PersonDatabase personDB;
    private ExecutorService executorService;
    private Handler mainThreadHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edName = view.findViewById(R.id.editTextText);
        edAge = view.findViewById(R.id.editTextText2);
        btnSave = view.findViewById(R.id.button);
        btnGet = view.findViewById(R.id.button2);

        // Инициализируем базу (используем requireContext() для контекста)
        personDB = PersonDatabase.getInstance(requireContext());

        executorService = Executors.newSingleThreadExecutor();
        mainThreadHandler = new Handler(Looper.getMainLooper());

        btnSave.setOnClickListener(v -> {
            String name = edName.getText().toString();
            String age = edAge.getText().toString();

            if (name.isEmpty() || age.isEmpty()) {
                //для вызова тоста тоже используем контекст, без него никак
                Toast.makeText(requireContext(), "Введите имя и возраст", Toast.LENGTH_SHORT).show();
                return;
            }

            Person person = new Person(name, age);
            addPersonInBackground(person);
        });

        btnGet.setOnClickListener(v -> getPersonListInBackground());
    }

    private void addPersonInBackground(Person person) {
        executorService.execute(() -> {
            personDB.getPersonDao().addPerson(person);
            mainThreadHandler.post(() -> {
                Toast.makeText(requireContext(), "Запись добавлена", Toast.LENGTH_LONG).show();
            });
        });
    }

    private void getPersonListInBackground() {
        executorService.execute(() -> {
            List<Person> personList = personDB.getPersonDao().getAllPersons();
            mainThreadHandler.post(() -> {
                if (personList.isEmpty()) {
                    Toast.makeText(requireContext(), "База данных пуста", Toast.LENGTH_LONG).show();
                    return;
                }

                StringBuilder sb = new StringBuilder();
                for (Person p : personList) {
                    sb.append(p.getName()).append(" : ").append(p.getAge()).append("\n");
                }

                Toast.makeText(requireContext(), sb.toString(), Toast.LENGTH_LONG).show();
            });
        });
    }
}