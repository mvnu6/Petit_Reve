package com.example.petit_reve;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Bundle;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Trouver le bouton "Générer l'histoire" dans le XML
        Button generateStoryButton = findViewById(R.id.generateStoryButton);

        // Définir un OnClickListener pour le bouton
        generateStoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer un Intent pour naviguer vers FormActivity
                Intent intent = new Intent(MainActivity.this, OpenAiActivity.class);
                startActivity(intent); // Démarrer FormActivity
            }
        });


        // Trouver le bouton "Histoire sauvegardée" dans le XML
        Button savedStoryButton = findViewById(R.id.savedStoryButton);
        savedStoryButton.setOnClickListener(v -> {
            // Créer un Intent pour naviguer vers StoryActivity
            Intent intent = new Intent(MainActivity.this, StoryActivity.class);
            startActivity(intent); // Démarrer l'activité StoryActivity
        });

// Récupérer l'ImageButton
        ImageButton menuButton = findViewById(R.id.menuButton);

        // Configurer l'action du bouton pour afficher le PopupMenu
        menuButton.setOnClickListener(v -> {
            // Créer un PopupMenu
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, menuButton);

            // Ajouter des éléments de menu directement dans le code
            popupMenu.getMenu().add("activer le son");
            popupMenu.getMenu().add("désactiver le son ");


            // Définir un listener pour les éléments du menu
            popupMenu.setOnMenuItemClickListener(item -> {
                // Utiliser le texte de l'élément pour déterminer l'option choisie
                String selectedOption = item.getTitle().toString();
                Toast.makeText(MainActivity.this, selectedOption + " sélectionnée", Toast.LENGTH_SHORT).show();
                return true;
            });

            // Afficher le menu
            popupMenu.show();
        });
    }
}


