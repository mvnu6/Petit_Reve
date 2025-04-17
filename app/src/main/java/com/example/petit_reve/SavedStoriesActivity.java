package com.example.petit_reve;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class SavedStoriesActivity extends AppCompatActivity {

    private ListView savedStoriesListView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_stories);

        savedStoriesListView = findViewById(R.id.saved_stories_list);
        backButton = findViewById(R.id.back_button);

        // Charger les histoires enregistrées
        ArrayList<String> savedStories = loadSavedStories();

        // Adapter pour afficher la liste des histoires
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, savedStories);
        savedStoriesListView.setAdapter(adapter);

        savedStoriesListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedStory = savedStories.get(position);
            // Redirige vers l'activité d'affichage de l'histoire sélectionnée
            showStoryDetails(selectedStory);
        });

        backButton.setOnClickListener(v -> finish()); // Retourner à l'activité principale
    }

    // Méthode pour charger les histoires enregistrées depuis le stockage interne
    private ArrayList<String> loadSavedStories() {
        ArrayList<String> savedStories = new ArrayList<>();
        File filesDir = getFilesDir();
        File[] files = filesDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".txt")) {
                    savedStories.add(file.getName().replace(".txt", ""));
                }
            }
        }
        return savedStories;
    }

    // Afficher les détails d'une histoire lorsqu'elle est sélectionnée
    private void showStoryDetails(String storyTitle) {
        Intent intent = new Intent(SavedStoriesActivity.this, StoryDetailActivity.class);
        intent.putExtra("STORY_TITLE", storyTitle);
        startActivity(intent);
    }
}
