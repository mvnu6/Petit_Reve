package com.example.petit_reve;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StoryActivity extends AppCompatActivity {

    private TextView storyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        // Lien vers le TextView où l'histoire sera affichée
        storyTextView = findViewById(R.id.storyTextView);

        // Récupérer l'histoire envoyée depuis OpenAiActivity
        String story = getIntent().getStringExtra("STORY");
        if (story != null) {
            storyTextView.setText(story);
        }
    }
}
