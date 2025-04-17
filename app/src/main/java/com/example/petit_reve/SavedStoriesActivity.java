package com.example.petit_reve;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class SavedStoriesActivity extends AppCompatActivity {

    private ListView savedStoriesListView;
    private Spinner storySelector;
    private Button readButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_stories);

        savedStoriesListView = findViewById(R.id.saved_stories_list);
        storySelector = findViewById(R.id.story_selector);
        readButton = findViewById(R.id.read_button);
        deleteButton = findViewById(R.id.delete_button);

        // Charger les histoires enregistrées
        ArrayList<String> savedStories = loadSavedStories();

        // Adapter pour afficher la liste des histoires dans le Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, savedStories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storySelector.setAdapter(adapter);

        // Charger les histoires dans la ListView
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, savedStories);
        savedStoriesListView.setAdapter(listAdapter);

        // Lire l'histoire sélectionnée
        readButton.setOnClickListener(v -> {
            String selectedStory = (String) storySelector.getSelectedItem();
            if (selectedStory != null) {
                // Afficher les détails de l'histoire
                showStoryDetails(selectedStory);
            } else {
                Toast.makeText(SavedStoriesActivity.this, "Veuillez sélectionner une histoire", Toast.LENGTH_SHORT).show();
            }
        });

        // Supprimer l'histoire sélectionnée avec confirmation
        deleteButton.setOnClickListener(v -> {
            String selectedStory = (String) storySelector.getSelectedItem();
            if (selectedStory != null) {
                showDeleteConfirmationDialog(selectedStory);
            } else {
                Toast.makeText(SavedStoriesActivity.this, "Veuillez sélectionner une histoire", Toast.LENGTH_SHORT).show();
            }
        });
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

    // Afficher un dialogue de confirmation avant de supprimer l'histoire
    private void showDeleteConfirmationDialog(String storyTitle) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation de suppression")
                .setMessage("Êtes-vous sûr de vouloir supprimer cette histoire ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    deleteStory(storyTitle);
                    dialog.dismiss();
                })
                .setNegativeButton("Non", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Méthode pour supprimer l'histoire
    private void deleteStory(String storyTitle) {
        File storyFile = new File(getFilesDir(), storyTitle + ".txt");
        if (storyFile.exists()) {
            if (storyFile.delete()) {
                Toast.makeText(SavedStoriesActivity.this, "Histoire supprimée", Toast.LENGTH_SHORT).show();
                // Actualiser la liste des histoires après la suppression
                ArrayList<String> savedStories = loadSavedStories();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, savedStories);
                storySelector.setAdapter(adapter);
            } else {
                Toast.makeText(SavedStoriesActivity.this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent musicIntent = new Intent(this, MusicService.class);
        startService(musicIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, MusicService.class));
    }
}
