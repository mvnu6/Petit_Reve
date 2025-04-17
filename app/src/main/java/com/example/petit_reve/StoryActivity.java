package com.example.petit_reve;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class StoryActivity extends AppCompatActivity {

    private TextView storyTitleTextView;
    private TextView storyContentTextView;
    private Button backButton;
    private Button shareButton;
    private Button favoriteButton;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        // Initialiser les vues
        storyTitleTextView = findViewById(R.id.story_title);
        storyContentTextView = findViewById(R.id.story_content);
        backButton = findViewById(R.id.back_button);
        shareButton = findViewById(R.id.share_button);
        favoriteButton = findViewById(R.id.favorite_button);

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

        // Configurer les boutons
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Retourner à l'activité précédente
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareStory(storyText);
            }
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
     * Partage l'histoire via les applications de partage disponibles
     *
     * @param story le texte de l'histoire à partager
     */
    private void shareStory(String story) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        String title = "Histoire personnalisée de Petit Rêve";
        String shareText = story;

        if (storyTitleTextView.getText() != null && !storyTitleTextView.getText().toString().isEmpty()) {
            title = storyTitleTextView.getText().toString();
        }

        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        startActivity(Intent.createChooser(shareIntent, "Partager l'histoire via"));
    }

    /**
     * Change l'état du bouton favori
     */
}