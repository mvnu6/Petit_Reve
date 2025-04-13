package com.example.petit_reve;

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

        Spinner ageSpinner = findViewById(R.id.ageSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.age_ranges,
                android.R.layout.simple_spinner_item
        );

        Spinner genderSpinner = findViewById(R.id.genderSpinner);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.gender_options, // Ajoute cette array dans `strings.xml`
                android.R.layout.simple_spinner_item
        );

        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(adapter);
        EditText typeInput = findViewById(R.id.typeInput);
        EditText nameInput = findViewById(R.id.nameInput);
        EditText keywordsInput = findViewById(R.id.keywordsInput);
        Button sendBtn = findViewById(R.id.sendBtn);
        TextView responseText = findViewById(R.id.responseText);
        ProgressBar loadingSpinner = findViewById(R.id.loadingSpinner);



        sendBtn.setOnClickListener(v -> {
            String age = ageSpinner.getSelectedItem().toString();
            String type = typeInput.getText().toString();
            String name = nameInput.getText().toString();
            String gender = genderSpinner.getSelectedItem().toString();
            String keywords = keywordsInput.getText().toString();

            String prompt = "Tu es un conteur spécialisé dans les histoires pour les tout-petits (âgés de 1 à 3 ans). Ton rôle est de **créer une histoire personnalisée**, douce et imagée, à partir des informations ci-dessous.\n" +
                    "\n" +
                    "Cette histoire est destinée à être lue ou écoutée par un très jeune enfant : elle doit être **simple, rassurante, tendre et remplie d’imaginaire**. Le ton est toujours **joyeux, bienveillant, avec une touche de magie**.\n" +
                    "\n" +
                    "**Contrainte importante** : l'histoire doit être composée de **3 à 4 paragraphes de longueur moyenne**, avec un langage accessible pour un tout-petit.\n" +
                    "\n" +
                    "Utilise des répétitions douces, des images tendres (nuages, étoiles, animaux, objets vivants), et évite toute complexité.\n" +
                    "\n" +
                    "Voici les informations fournies par l'utilisateur :\n" +
                    "\n" +
                    "- 🧒 **Nom du héros :** " + name + "\n" +
                    "- 🎂 **Âge de l'enfant :** " + age + "\n" +
                    "- 💬 **Type de récit :** " + type + "\n" +
                    "-    **genre de l'enfant:**" + gender +"\n" +
                    "- 🌍 **Lieu magique :** forêt enchantée (par défaut)\n" +
                    "- 🧸 **Personnage préféré :** petit chat (par défaut)\n" +
                    "- 🌟 **Mot-clé ou objet spécial :** " + keywords + "\n" +
                    "\n" +
                    "---\n" +
                    "\n" +
                    "🔊 **Consignes supplémentaires :**\n" +
                    "\n" +
                    "- Commence par une introduction tendre et immersive.\n" +
                    "- Développe une aventure simple mais poétique, adaptée à un jeune enfant.\n" +
                    "- Termine par une conclusion douce, qui rassure et invite au sommeil ou au rêve.\n" +
                    "- Adopte un ton pastel et rêveur à l’image de l’univers de l’application **Petit Rêve**.\n" +
                    "\n" +
                    "---\n" +
                    "\n" +
                    "✨ **Format de la réponse attendu :**\n" +
                    "\n" +
                    "Titre : {un titre doux, imagé et adapté aux enfants}\n" +
                    "\n" +
                    "Paragraphe 1 – Introduction tendre : présentation du héros et du décor magique.\n" +
                    "Paragraphe 2 – Début de la petite aventure : un événement calme mais merveilleux.\n" +
                    "Paragraphe 3 – Suite et fin de l’aventure, avec une touche de poésie et de magie.\n" +
                    "Paragraphe 4 (facultatif) – Conclusion très douce, qui invite au calme ou au sommeil.\n" +
                    "\n" +
                    "**Attention** : Si certaines réponses sont inappropriées, incomplètes ou non adaptées à un très jeune public (1 à 3 ans), **ignore-les** ou **remplace-les automatiquement** par des éléments neutres, bienveillants et adaptés.\n" +
                    "\n" +
                    "Tu dois TOUJOURS produire une **histoire douce, calme et rassurante**, adaptée aux enfants en crèche.";


            sendBtn.setEnabled(false); // désactive le bouton
            loadingSpinner.setVisibility(View.VISIBLE); // affiche la roue de chargement

            new Thread(() -> {
                try {
                    System.out.println("🟢 Envoi de la requête à OpenAI...");
                    String response = aiService.getResponse(prompt);
                    runOnUiThread(() -> {
                        responseText.setText(response);
                        sendBtn.setEnabled(true);
                        loadingSpinner.setVisibility(View.GONE);
                        System.out.println("✅ Réponse reçue !");
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        responseText.setText("Erreur : " + e.getMessage());
                        sendBtn.setEnabled(true);
                        loadingSpinner.setVisibility(View.GONE);
                        System.out.println("❌ Erreur : " + e.getMessage());
                    });
                    e.printStackTrace();
                }
            }).start();
        });
    }
}
