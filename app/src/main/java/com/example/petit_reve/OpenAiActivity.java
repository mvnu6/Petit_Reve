package com.example.petit_reve;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

public class OpenAiActivity extends AppCompatActivity {


    private OpenAiService aiService = new OpenAiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openai);

        Spinner storySpinner = findViewById(R.id.storySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.story_options,
                android.R.layout.simple_spinner_item
        );

        Spinner substorySpinner = findViewById(R.id.substorySpinner);
        ArrayAdapter<CharSequence> substoryadapter = ArrayAdapter.createFromResource(
                this,
                R.array.substory_options,
                android.R.layout.simple_spinner_item
        );

        Spinner genderSpinner = findViewById(R.id.genderSpinner);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.gender_options,
                android.R.layout.simple_spinner_item
        );

        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storySpinner.setAdapter(adapter);
        substoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        substorySpinner.setAdapter(substoryadapter);
        EditText ageInput = findViewById(R.id.ageInput);
        EditText nameInput = findViewById(R.id.nameInput);
        EditText keywordsInput = findViewById(R.id.keywordsInput);
        EditText placeInput = findViewById(R.id.placeInput);
        EditText characterInput = findViewById(R.id.characterInput);
        Button sendBtn = findViewById(R.id.sendBtn);
        ProgressBar loadingSpinner = findViewById(R.id.loadingSpinner);



        sendBtn.setOnClickListener(v -> {
            String story = storySpinner.getSelectedItem().toString();
            String substory = substorySpinner.getSelectedItem().toString();
            String age = ageInput.getText().toString();
            String place = placeInput.getText().toString();
            String favcharacter = characterInput.getText().toString();
            String name = nameInput.getText().toString();
            String gender = genderSpinner.getSelectedItem().toString();
            String keywords = keywordsInput.getText().toString();

            String prompt = "Tu es un conteur spécialisé dans les histoires pour les tout-petits (âgés de 1 à 3 ans). Ton rôle est de créer une histoire personnalisée, douce et imagée, à partir des informations ci-dessous.\n" +
                    "\n" +
                    "Cette histoire est destinée à être lue ou écoutée par un très jeune enfant : elle doit être simple, rassurante, tendre et remplie d’imaginaire. Le ton est toujours joyeux, bienveillant, avec une touche de magie.\n" +
                    "\n" +
                    "Contrainte importante : l'histoire doit être composée de 3 à 4 paragraphes de longueur moyenne, avec un langage accessible pour un tout-petit.\n" +
                    "\n" +
                    "Utilise des répétitions douces, des images tendres (nuages, étoiles, animaux, objets vivants), et évite toute complexité.\n" +
                    "\n" +
                    "Voici les informations fournies par l'utilisateur :\n" +
                    "\n" +
                    "- Nom du héros : " + name + "\n" +
                    "- Age du héros : " + age + "\n" +
                    "- Genre de l'enfant : " + gender +"\n" +
                    "- Type de récit : " + story + "\n" +
                    "- Type d'histoire : " + substory + "\n" +
                    "- Lieu magique : " + place + "\n" +
                    "- Personnage préféré : " + favcharacter + "\n" +
                    "- Mot-clé ou objet spécial : " + keywords + "\n" +
                    "\n" +
                    "---\n" +
                    "\n" +
                    "🔊 Consignes supplémentaires :\n" +
                    "\n" +
                    "- Commence par une introduction tendre et immersive.\n" +
                    "- Développe une aventure simple mais poétique, adaptée à un jeune enfant.\n" +
                    "- Termine par une conclusion douce, qui rassure et invite au sommeil ou au rêve.\n" +
                    "\n" +
                    "---\n" +
                    "\n" +
                    "✨ **Format de la réponse attendu :**\n" +
                    "\n" +
                    "Titre : {un titre doux, imagé et adapté aux enfants}\n" +
                    "\n" +
                    "Présentation du héros et du décor magique.\n" +
                    "Un événement calme mais merveilleux.\n" +
                    "Suite et fin de l’aventure, avec une touche de poésie et de magie.\n" +
                    "Conclusion très douce, qui invite au calme ou au sommeil.\n" +
                    "\n" +
                    "**Attention** : Si certaines réponses sont inappropriées, incomplètes ou non adaptées à un très jeune public (1 à 3 ans), **ignore-les** ou **remplace-les automatiquement** par des éléments neutres, bienveillants et adaptés.\n" +
                    "\n" +
                    "Tu dois TOUJOURS produire une **histoire douce, calme et rassurante**, adaptée aux enfants en crèche.";


            sendBtn.setEnabled(false); // désactive le bouton
            loadingSpinner.setVisibility(View.VISIBLE); // affiche la roue de chargement

            new Thread(() -> {
                try {
                    String response = aiService.getResponse(prompt);

                    // Envoi de l'histoire à la nouvelle activité
                    Intent intent = new Intent(OpenAiActivity.this, StoryActivity.class);
                    intent.putExtra("STORY", response);

                    // Démarrer la nouvelle activité avec l'histoire
                    startActivity(intent);

                    runOnUiThread(() -> {
                        loadingSpinner.setVisibility(View.GONE);
                        sendBtn.setEnabled(true);
                    });

                } catch (Exception e) {
                    runOnUiThread(() -> {
                        sendBtn.setEnabled(true);
                        loadingSpinner.setVisibility(View.GONE);
                    });
                    e.printStackTrace();
                }
            }).start();
        });
    }
}
