package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText editTextMovieName, editTextRating;
    private Spinner spinnerGenre;
    private Button buttonAdd;
    private ListView listViewMovies;

    private ArrayList<String> movieList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private static String PREFS_NAME = "MovieAppPrefs";
    private static String MOVIE_LIST_KEY = "MovieList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextMovieName = findViewById(R.id.editTextMovieName);
        editTextRating = findViewById(R.id.editTextRating);
        spinnerGenre = findViewById(R.id.spinnerGenre);
        buttonAdd = findViewById(R.id.buttonAdd);
        listViewMovies = findViewById(R.id.listViewMovies);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, movieList);
        listViewMovies.setAdapter(adapter);

        loadMovies();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMovie();
            }
        });
    }

    private void addMovie() {
        String movieName = editTextMovieName.getText().toString().trim();
        String ratingText = editTextRating.getText().toString().trim();
        String genre = spinnerGenre.getSelectedItem().toString();

        if (movieName.isEmpty() || ratingText.isEmpty()) {
            Toast.makeText(this, "Uzupełnij wszystkie pola!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int rating = Integer.parseInt(ratingText);
            if (rating < 1 || rating > 5) throw new Exception();

            String movieEntry = movieName + " (" + genre + ") - Ocena: " + rating;
            movieList.add(movieEntry);
            adapter.notifyDataSetChanged();

            saveMovies();
            editTextMovieName.setText("");
            editTextRating.setText("");
        } catch (Exception e) {
            Toast.makeText(this, "Ocena musi być liczbą od 1 do 5", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveMovies() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> set = new HashSet<>(movieList);
        editor.putStringSet(MOVIE_LIST_KEY, set);
        editor.apply();
    }

    private void loadMovies() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> set = prefs.getStringSet(MOVIE_LIST_KEY, new HashSet<>());
        movieList.clear();
        movieList.addAll(set);
        adapter.notifyDataSetChanged();
    }
}
