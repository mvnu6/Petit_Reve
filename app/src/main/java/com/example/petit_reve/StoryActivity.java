package com.example.petit_reve;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class StoryActivity extends AppCompatActivity {

    private TextView storyTitleTextView;
    private TextView storyContentTextView;
    private Button backButton;
    private Button favoriteButton;
    private Button nextButton;
    private Button prevButton;
    private Button saveButton;
    private ImageView storyImageView;

    private String[] storyParagraphs;
    private int currentParagraphIndex = 0;

    private String[] imageNames = {
            "football_3", "hero", "hero_2", "licorne", "licorne_2", "licorne_3", "pirate", "pirate_2", "pirate_3", "pirate_4"
    };

    private boolean isFavorite = false;
    private Handler handler = new Handler();

    private ArrayList<String> displayedImages = new ArrayList<>();
    private ArrayList<String> displayedTexts = new ArrayList<>();

    // Indique si l'histoire provient du stockage (déjà sauvegardée)
    private boolean isFromStorage = false;

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
        saveButton = findViewById(R.id.save_button);
        storyImageView = findViewById(R.id.story_image);

        nextButton.setEnabled(false);
        prevButton.setEnabled(false);

        // Vérifier si nous avons une nouvelle histoire ou si nous chargeons une histoire sauvegardée
        String storyText = getIntent().getStringExtra("STORY");
        String storyTitle = getIntent().getStringExtra("STORY_TITLE");

        if (storyText != null && !storyText.isEmpty()) {
            // Cas 1: Nouvelle histoire générée
            handleNewStory(storyText);
        } else if (storyTitle != null && !storyTitle.isEmpty()) {
            // Cas 2: Histoire sauvegardée à charger
            isFromStorage = true;
            handleSavedStory(storyTitle);
        } else {
            Toast.makeText(this, "L'histoire est vide ou n'a pas été transmise correctement.", Toast.LENGTH_SHORT).show();
            return;
        }

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

        // Logique pour le bouton favori (renommé en saveButton dans certaines parties du code)
        favoriteButton.setOnClickListener(v -> {
            isFavorite = !isFavorite;
            favoriteButton.setText(isFavorite ? "Retirer des favoris" : "Ajouter aux favoris");
        });

        // Logique pour le bouton de sauvegarde
        saveButton.setOnClickListener(v -> {
            if (isFromStorage) {
                Toast.makeText(this, "Cette histoire est déjà sauvegardée", Toast.LENGTH_SHORT).show();
            } else {
                // Reconstruire tout le contenu de l'histoire à partir des paragraphes
                StringBuilder fullContent = new StringBuilder();
                for (String paragraph : storyParagraphs) {
                    fullContent.append(paragraph).append("\n\n");
                }

                String titleToSave = storyTitleTextView.getText().toString();
                saveStoryLocally(titleToSave, fullContent.toString().trim());

                // Indiquer que l'histoire est maintenant sauvegardée
                isFromStorage = true;
                saveButton.setText("Histoire sauvegardée");
                saveButton.setEnabled(false);
            }
        });
    }

    /**
     * Gère l'affichage d'une nouvelle histoire
     *
     * @param storyText le texte complet de l'histoire
     */
    private void handleNewStory(String storyText) {
        String[] storyParts = extractTitleAndContent(storyText);
        String title = storyParts[0];
        String content = storyParts[1];

        // Afficher le titre
        storyTitleTextView.setText(title);

        // Diviser le contenu en paragraphes
        storyParagraphs = content.split("\n\n");

        // Afficher le premier paragraphe
        displayTextProgressively(storyParagraphs[currentParagraphIndex]);

        // Configurer le bouton de sauvegarde pour une nouvelle histoire
        saveButton.setText("Sauvegarder l'histoire");
        saveButton.setEnabled(true);
    }

    /**
     * Gère le chargement et l'affichage d'une histoire sauvegardée
     *
     * @param storyTitle le titre de l'histoire à charger
     */
    private void handleSavedStory(String storyTitle) {
        try {
            // Ouvrir le fichier de l'histoire sauvegardée
            FileInputStream fis = openFileInput(storyTitle + ".txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            // Fermer les flux
            bufferedReader.close();
            isr.close();
            fis.close();

            // Extraire le titre et le contenu du texte chargé
            String fileContent = sb.toString();
            String[] storyParts = extractTitleAndContent(fileContent);

            // Afficher le titre
            storyTitleTextView.setText(storyTitle);

            // Diviser le contenu en paragraphes
            storyParagraphs = storyParts[1].split("\n\n");

            // Afficher le premier paragraphe
            displayTextProgressively(storyParagraphs[currentParagraphIndex]);

            // Configurer le bouton de sauvegarde pour une histoire déjà sauvegardée
            saveButton.setText("Histoire sauvegardée");
            saveButton.setEnabled(false);

        } catch (IOException e) {
            e.printStackTrace();
            storyTitleTextView.setText(storyTitle);
            storyContentTextView.setText("Erreur lors du chargement de l'histoire.");

            // Désactiver les boutons de navigation
            nextButton.setEnabled(false);
            prevButton.setEnabled(false);
        }
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
                    nextButton.setEnabled(currentParagraphIndex < storyParagraphs.length - 1);
                    prevButton.setEnabled(currentParagraphIndex > 0);
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

        // Sauvegarder le texte affiché si ce n'est pas déjà fait
        if (currentParagraphIndex >= displayedTexts.size()) {
            displayedTexts.add(finalText);
        }
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
}