package com.example.petit_reve;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class OpenAiActivity extends AppCompatActivity {

    private OpenAIService aiService = new OpenAIService();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openai);  // Assurez-vous que votre activité est bien liée au bon XML

        // Initialisation des éléments de l'interface
        Spinner storySpinner = findViewById(R.id.storySpinner);
        Spinner substorySpinner = findViewById(R.id.substorySpinner);
        Spinner genderSpinner = findViewById(R.id.genderSpinner);

        EditText ageInput = findViewById(R.id.ageInput);
        EditText nameInput = findViewById(R.id.nameInput);
        EditText keywordsInput = findViewById(R.id.keywordsInput);
        EditText placeInput = findViewById(R.id.placeInput);
        EditText characterInput = findViewById(R.id.characterInput);
        Button sendBtn = findViewById(R.id.sendBtn);
        ProgressBar loadingSpinner = findViewById(R.id.loadingSpinner);

        // Récupérer le bouton de menu dans le header inclus
        ImageButton menuButton = findViewById(R.id.menuButton);  // Assurez-vous que l'id correspond à celui du bouton dans activity_header.xml

        // Définir l'action du bouton de menu
        menuButton.setOnClickListener(v -> {
            // Afficher le menu
            MenuActivity.showMenu(OpenAiActivity.this, v); // Appel à la méthode showMenu de MenuActivity pour afficher le menu
        });

        // Ajouter le logo pour redirection vers MainActivity
        ImageView logoButton = findViewById(R.id.logoImage); // Récupérer l'ID du logo dans le header

        // Définir l'action du logo pour rediriger vers l'activité principale
        logoButton.setOnClickListener(v -> {
            // Créer un Intent pour ouvrir l'activité principale (MainActivity)
            Intent intent = new Intent(OpenAiActivity.this, MainActivity.class);
            startActivity(intent);  // Démarrer l'activité principale
        });

        // Adapter principal pour le type de récit
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.story_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storySpinner.setAdapter(adapter);

        // Adapter pour le genre
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.gender_options,
                android.R.layout.simple_spinner_item
        );
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        // Mise à jour dynamique du sous-type d’histoire en fonction du type de récit
        storySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStoryType = parent.getItemAtPosition(position).toString();
                int substoryArrayRes;

                if (selectedStoryType.equals("Aventure")) {
                    substoryArrayRes = R.array.substory_aventure_options;
                } else if (selectedStoryType.equals("Comptine")) {
                    substoryArrayRes = R.array.substory_comptine_options;
                } else {
                    substoryArrayRes = R.array.substory_aventure_options;
                }

                ArrayAdapter<CharSequence> substoryAdapter = ArrayAdapter.createFromResource(
                        OpenAiActivity.this,
                        substoryArrayRes,
                        android.R.layout.simple_spinner_item
                );
                substoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                substorySpinner.setAdapter(substoryAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Génération de l’histoire
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
                    "Si le type de récit est : Comptine, ignore toutes les histoires et génère uniquement une comptine. Ne tiens pas compte de l'âge. crée une comptine rythmée et chantante pour un enfant de crèche (0-3 ans) voici un exemple Exemple de comptine :\n" +
                    "Titre : \"Le petit oiseau chante\"\n" +
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

            sendBtn.setEnabled(false);
            loadingSpinner.setVisibility(View.VISIBLE);

            new Thread(() -> {
                try {
                    String response = aiService.getResponse(prompt);


                    // Envoi de l'histoire à la nouvelle activité
                    Intent intent = new Intent(OpenAiActivity.this, StoryActivity.class);
                    intent.putExtra("STORY", response);
                    intent.putExtra("STORY_TYPE", story); // Ajout de la valeur de story

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

    // Méthode pour sauvegarder l'histoire dans un fichier local
    public void saveStoryToFile(String storyTitle, String storyContent) {
        try {
            // Utilisation de l'encodage UTF-8 pour le nom de fichier
            String fileName = storyTitle + ".txt";  // Par exemple: "Mon histoire spéciale.txt"
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
            writer.write(storyContent);  // Enregistrer le contenu de l'histoire
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
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
