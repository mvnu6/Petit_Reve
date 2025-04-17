package com.example.petit_reve;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class StoryDetailActivity extends AppCompatActivity {

    private TextView storyDetailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        storyDetailTextView = findViewById(R.id.story_detail_text);

        // Ajouter l'ImageView pour le logo
        ImageView logoButton = findViewById(R.id.logoImage);  // Récupérer l'ID du logo dans le header

        // Définir l'action du logo pour rediriger vers l'activité principale
        logoButton.setOnClickListener(v -> {
            // Créer un Intent pour ouvrir l'activité principale (MainActivity)
            Intent intent = new Intent(StoryDetailActivity.this, MainActivity.class);
            startActivity(intent);  // Démarrer l'activité principale
        });

        // Récupérer l'histoire de l'intent
        String storyTitle = getIntent().getStringExtra("STORY_TITLE");
        if (storyTitle != null) {
            // Charger le contenu de l'histoire depuis le fichier
            loadStoryDetails(storyTitle);
        }

        // Récupérer le bouton de menu dans le header inclus
        ImageButton menuButton = findViewById(R.id.menuButton);  // Assurez-vous que l'id correspond à celui du bouton dans activity_header.xml

        // Définir l'action du bouton de menu
        menuButton.setOnClickListener(v -> {
            // Afficher le menu
            MenuActivity.showMenu(StoryDetailActivity.this, v); // Appel à la méthode showMenu de MenuActivity pour afficher le menu
        });
    }

    // Méthode pour charger le contenu d'une histoire depuis le fichier
    private void loadStoryDetails(String storyTitle) {
        try {
            // Utilisation de InputStreamReader avec UTF-8 pour gérer les caractères spéciaux
            FileInputStream fis = openFileInput(storyTitle + ".txt");
            InputStreamReader reader = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder storyContent = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                storyContent.append(line).append("\n");
            }
            bufferedReader.close();
            storyDetailTextView.setText(storyContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
            storyDetailTextView.setText("Erreur lors du chargement de l'histoire.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, MusicService.class));
    }
}
