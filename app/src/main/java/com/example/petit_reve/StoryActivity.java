package com.example.petit_reve;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class StoryActivity extends AppCompatActivity {

    private TextView storyTitleTextView;
    private TextView storyContentTextView;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        // Initialiser les vues
        storyTitleTextView = findViewById(R.id.story_title);
        storyContentTextView = findViewById(R.id.story_content);
        saveButton = findViewById(R.id.save_button);

        // Récupérer l'histoire de l'intent
        String storyText = getIntent().getStringExtra("STORY");

        if (storyText != null && !storyText.isEmpty()) {
            // Extraire le titre et le contenu
            String[] storyParts = extractTitleAndContent(storyText);
            String title = storyParts[0];
            String content = storyParts[1];

            // Afficher le titre et le contenu
            storyTitleTextView.setText(title);
            storyContentTextView.setText(content);
        } else {
            storyTitleTextView.setText("Histoire non disponible");
            storyContentTextView.setText("Désolé, aucune histoire n'a pu être générée. Veuillez réessayer.");
        }

        // Configurer le bouton de sauvegarde
        saveButton.setOnClickListener(v -> {
            String title = storyTitleTextView.getText().toString();
            String content = storyContentTextView.getText().toString();

            // Enregistre l'histoire localement
            saveStoryLocally(title, content);
        });
    }

    /**
     * Extrait le titre et le contenu de l'histoire à partir du texte complet
     *
     * @param fullStory le texte complet de l'histoire
     * @return un tableau avec le titre en position 0 et le contenu en position 1
     */
    private String[] extractTitleAndContent(String fullStory) {
        String[] result = new String[2];
        String title = "Histoire Personnalisée";
        String content = fullStory;

        // Rechercher le format "Titre : XYZ" dans le texte
        if (fullStory.contains("Titre :") || fullStory.contains("Titre:")) {
            String[] lines = fullStory.split("\n");
            for (String line : lines) {
                if (line.trim().startsWith("Titre :") || line.trim().startsWith("Titre:")) {
                    title = line.replace("Titre :", "").replace("Titre:", "").trim();
                    // Utilisation de substring pour tout ce qui suit le titre
                    content = fullStory.substring(fullStory.indexOf(line) + line.length()).trim();
                    break;
                }
            }
        }

        // Vérifie simplement que l'histoire n'est pas vide
        if (content.length() > 0) {
            result[0] = title;
            result[1] = content;
        } else {
            // Si l'histoire est vide, afficher un message d'erreur
            result[0] = title;
            result[1] = "L'histoire n'a pas pu être générée correctement.";
        }

        return result;
    }

    /**
     * Enregistre l'histoire localement
     *
     * @param title   Le titre de l'histoire
     * @param content Le contenu de l'histoire
     */
    private void saveStoryLocally(String title, String content) {
        String filename = title + ".txt"; // Utilise le titre de l'histoire pour le nom du fichier
        String fileContents = "Titre: " + title + "\n\n" + content;

        try {
            // Ouvre un fichier pour écrire dans le stockage interne
            FileOutputStream fos = openFileOutput(filename, MODE_PRIVATE);
            fos.write(fileContents.getBytes());
            fos.close();

            // Affiche un message confirmant l'enregistrement
            Toast.makeText(this, "Histoire enregistrée localement", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de l'enregistrement de l'histoire", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent musicIntent = new Intent(this, MusicService.class);
        musicIntent.putExtra("MUSIC_FILE", R.raw.musique_aventure); // Remplacez par votre fichier
        startService(musicIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, MusicService.class));
    }
}
