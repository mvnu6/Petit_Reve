package com.example.petit_reve;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class StoryActivity extends AppCompatActivity {

    private TextView storyTitleTextView;
    private TextView storyContentTextView;
    private Button backButton;
    private Button favoriteButton;
    private Button nextButton;
    private Button prevButton;
    private Button saveButton;  // Nouveau bouton pour sauvegarder l'histoire
    private ImageView storyImageView;

    private String[] storyParagraphs;
    private int currentParagraphIndex = 0; // pour suivre quel paragraphe est affiché

    private String[] imageNames = {
            "football_3", "hero", "hero_2", "licorne", "licorne_2", "licorne_3", "pirate", "pirate_2", "pirate_3", "pirate_4"
    };

    private boolean isFavorite = false;
    private Handler handler = new Handler();

    // Liste pour mémoriser les images et textes affichés pour chaque paragraphe
    private ArrayList<String> displayedImages = new ArrayList<>();
    private ArrayList<String> displayedTexts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        // Initialiser les vues
        storyTitleTextView = findViewById(R.id.story_title);
        storyContentTextView = findViewById(R.id.story_content);
        backButton = findViewById(R.id.back_button);
        favoriteButton = findViewById(R.id.save_button);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        saveButton = findViewById(R.id.save_button);  // Initialisation du bouton de sauvegarde
        storyImageView = findViewById(R.id.story_image);

        nextButton.setEnabled(false);
        prevButton.setEnabled(false);

        // Récupérer l'histoire de l'intent
        String storyText = getIntent().getStringExtra("STORY");

        if (storyText == null || storyText.isEmpty()) {
            Toast.makeText(this, "L'histoire est vide ou n'a pas été transmise correctement.", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] storyParts = extractTitleAndContent(storyText);
        String title = storyParts[0];
        String content = storyParts[1];

        // Afficher le titre et le contenu
        storyTitleTextView.setText(title);
        storyParagraphs = content.split("\n\n");  // Diviser en paragraphes

        // Affichage du texte de manière progressive
        displayTextProgressively(storyParagraphs[currentParagraphIndex]);

        // Configurer les boutons
        backButton.setOnClickListener(v -> finish());  // Retourner à l'activité précédente

        nextButton.setOnClickListener(v -> {
            if (currentParagraphIndex < storyParagraphs.length - 1) {
                currentParagraphIndex++;
                displayTextProgressively(storyParagraphs[currentParagraphIndex]);
            } else {
                Toast.makeText(this, "C'est le dernier paragraphe", Toast.LENGTH_SHORT).show();
            }
        });

        prevButton.setOnClickListener(v -> {
            if (currentParagraphIndex > 0) {
                currentParagraphIndex--;
                displayTextProgressively(storyParagraphs[currentParagraphIndex]);
            } else {
                Toast.makeText(this, "C'est le premier paragraphe", Toast.LENGTH_SHORT).show();
            }
        });

        // Logique pour le bouton favori
        favoriteButton.setOnClickListener(v -> {
            isFavorite = !isFavorite;
            favoriteButton.setText(isFavorite ? "Retirer des favoris" : "Ajouter aux favoris");
        });

        // Logique pour le bouton de sauvegarde
        saveButton.setOnClickListener(v -> {
            String titleToSave = storyTitleTextView.getText().toString();
            String contentToSave = storyContentTextView.getText().toString();
            saveStoryLocally(titleToSave, contentToSave);
        });
    }

    private void displayTextProgressively(String paragraph) {
        // Désactiver les boutons pendant la génération du texte
        nextButton.setEnabled(false);
        prevButton.setEnabled(false);

        // Réinitialiser le texte avant de l'afficher
        storyContentTextView.setText("");

        // Diviser le paragraphe en caractères
        final String finalText = paragraph;
        final int length = finalText.length();

        // Afficher le texte progressivement, caractère par caractère
        for (int i = 0; i < length; i++) {
            final int index = i;
            handler.postDelayed(() -> {
                storyContentTextView.append(String.valueOf(finalText.charAt(index)));

                // Si on a atteint le dernier caractère, on réactive les boutons
                if (index == length - 1) {
                    backButton.setEnabled(true);
                    nextButton.setEnabled(true);
                    prevButton.setEnabled(true);
                }
            }, 30 * i); // Afficher un caractère toutes les 30ms
        }

        // Si l'image a déjà été affichée pour ce paragraphe, la réutiliser
        if (currentParagraphIndex < displayedImages.size()) {
            setImage(displayedImages.get(currentParagraphIndex));
        } else {
            // Sélectionner aléatoirement une image pour ce paragraphe
            String randomImageName = getRandomImageName();
            displayedImages.add(randomImageName); // Sauvegarder l'image pour ce paragraphe
            setImage(randomImageName);
        }

        // Sauvegarder le texte affiché
        displayedTexts.add(finalText);
    }

    private void setImage(String imageName) {
        int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        if (imageResId != 0) {
            storyImageView.setImageResource(imageResId);
        }
    }

    private String getRandomImageName() {
        Random random = new Random();
        return imageNames[random.nextInt(imageNames.length)];  // Sélectionne un nom d'image aléatoire
    }

    private String[] extractTitleAndContent(String fullStory) {
        String[] result = new String[2];
        String title = "Histoire Personnalisée";
        String content = fullStory;

        if (fullStory.contains("Titre :") || fullStory.contains("Titre:")) {
            String[] lines = fullStory.split("\n");
            for (String line : lines) {
                if (line.trim().startsWith("Titre :") || line.trim().startsWith("Titre:")) {
                    title = line.replace("Titre :", "").replace("Titre:", "").trim();
                    content = fullStory.substring(fullStory.indexOf(line) + line.length()).trim();
                    break;
                }
            }
        }

        result[0] = title;
        result[1] = content.length() > 0 ? content : "L'histoire n'a pas pu être générée correctement.";
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

        // Récupérer la variable "story" depuis l'intent
        String storyType = getIntent().getStringExtra("STORY_TYPE");
        Log.d("StoryActivity", "Valeur de storyType : " + storyType);

        // Définir les tableaux de musiques
        String[] aventureMusics = {"aventure1", "aventure2", "aventure3", "aventure4"};
        String[] comptineMusics = {"comptine1", "comptine2", "comptine3", "comptine4", "comptine5"};

        // Sélectionner une musique aléatoire en fonction du type de récit
        String selectedMusic;
        if ("Aventure".equalsIgnoreCase(storyType)) {
            selectedMusic = aventureMusics[new Random().nextInt(aventureMusics.length)];
        } else {
            selectedMusic = comptineMusics[new Random().nextInt(comptineMusics.length)];
        }

        // Obtenir l'ID de la ressource pour la musique sélectionnée
        int musicResId = getResources().getIdentifier(selectedMusic, "raw", getPackageName());

        // Démarrer le service de musique avec la musique sélectionnée
        Intent musicIntent = new Intent(this, MusicService.class);
        musicIntent.putExtra("MUSIC_FILE", musicResId);
        startService(musicIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, MusicService.class));
    }
}
