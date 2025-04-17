package com.example.petit_reve;

import android.os.Bundle;
import android.widget.TextView;

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

        String storyTitle = getIntent().getStringExtra("STORY_TITLE");
        if (storyTitle != null) {
            // Charger le contenu de l'histoire depuis le fichier
            loadStoryDetails(storyTitle);
        }
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
}
