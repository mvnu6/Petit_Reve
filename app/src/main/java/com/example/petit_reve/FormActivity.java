package com.example.petit_reve;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FormActivity extends AppCompatActivity {

    private EditText editNom, editAge, editLieu, editPersonnage, editObjet;
    private RadioGroup radioTypeRecit;
    private Button btnGenerer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        // Initialisation des champs
        editNom = findViewById(R.id.editNom);
        editAge = findViewById(R.id.editAge);
        editLieu = findViewById(R.id.editLieu);
        editPersonnage = findViewById(R.id.editPersonnage);
        editObjet = findViewById(R.id.editObjet);
        radioTypeRecit = findViewById(R.id.radioTypeRecit);
        btnGenerer = findViewById(R.id.btnGenerer);



        // Bouton génération
        btnGenerer.setOnClickListener(v -> {
            String nom = editNom.getText().toString().trim();
            String age = editAge.getText().toString().trim();
            String lieu = editLieu.getText().toString().trim();
            String personnage = editPersonnage.getText().toString().trim();
            String objet = editObjet.getText().toString().trim();

            int selectedId = radioTypeRecit.getCheckedRadioButtonId();
            RadioButton selectedBtn = findViewById(selectedId);
            String typeRecit = selectedBtn != null ? selectedBtn.getText().toString() : "histoire";

            // Envoi vers génération d'histoire (API)
            Intent intent = new Intent(FormActivity.this, StoryActivity.class);
            intent.putExtra("nomHeros", nom);
            intent.putExtra("age", age);
            intent.putExtra("typeRecit", typeRecit);
            intent.putExtra("lieu", lieu);
            intent.putExtra("personnagePrefere", personnage);
            intent.putExtra("motCleSpecial", objet);
            startActivity(intent);
        });
    }
}
