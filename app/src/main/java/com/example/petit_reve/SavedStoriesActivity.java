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
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

        // Configuration du bouton retour s'il existe
        ImageButton backButton = findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        // Configuration du bouton menu s'il existe
        ImageButton menuButton = findViewById(R.id.menuButton);
        if (menuButton != null) {
            menuButton.setOnClickListener(v -> {
                MenuActivity.showMenu(SavedStoriesActivity.this, v);
            });
        }

        // Charger les histoires enregistrées
        ArrayList<String> savedStories = loadSavedStories();

        // Adapter pour afficher la liste des histoires dans le Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, savedStories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storySelector.setAdapter(adapter);

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
        // Nouvelle approche : lire le contenu du fichier et le passer directement
        String storyContent = readStoryContent(storyTitle);

        Intent intent = new Intent(SavedStoriesActivity.this, StoryActivity.class);
        intent.putExtra("STORY_TITLE", storyTitle);  // Passage du titre de l'histoire
        // Facultatif : déterminer le type d'histoire pour la musique
        intent.putExtra("STORY_TYPE", determineStoryType(storyContent));

        startActivity(intent);  // Lancer StoryActivity
    }

    // Méthode pour lire le contenu d'une histoire
    private String readStoryContent(String storyTitle) {
        StringBuilder content = new StringBuilder();
        try {
            FileInputStream fis = openFileInput(storyTitle + ".txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de la lecture du fichier", Toast.LENGTH_SHORT).show();
        }
        return content.toString();
    }

    // Méthode pour déterminer le type d'histoire (pour la musique)
    private String determineStoryType(String content) {
        // Simple heuristique : si le contenu contient des mots liés à l'aventure
        String lowerContent = content.toLowerCase();
        if (lowerContent.contains("aventure") ||
                lowerContent.contains("pirate") ||
                lowerContent.contains("héros") ||
                lowerContent.contains("hero") ||
                lowerContent.contains("dragon") ||
                lowerContent.contains("chevalier")) {
            return "Aventure";
        } else {
            return "Comptine";
        }
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
}