package com.example.petit_reve;

import android.content.Context;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MenuActivity {

    // Variable pour garder la trace de l'état du son (par défaut activé)
    private static boolean isSoundEnabled = true;

    public static void showMenu(Context context, View v) {
        // Créer un PopupMenu
        PopupMenu popupMenu = new PopupMenu(context, v);

        // Ajouter les options au menu
        popupMenu.getMenu().add("Activer le son");
        popupMenu.getMenu().add("Désactiver le son");

        // Définir un listener pour les éléments du menu
        popupMenu.setOnMenuItemClickListener(item -> {
            String selectedOption = item.getTitle().toString();

            // Gérer l'activation ou la désactivation du son
            if (selectedOption.equals("Activer le son")) {
                if (!isSoundEnabled) {
                    isSoundEnabled = true;  // Activer le son
                    Toast.makeText(context, "Son activé", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Le son est déjà activé", Toast.LENGTH_SHORT).show();
                }
            } else if (selectedOption.equals("Désactiver le son")) {
                if (isSoundEnabled) {
                    isSoundEnabled = false;  // Désactiver le son
                    Toast.makeText(context, "Son désactivé", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Le son est déjà désactivé", Toast.LENGTH_SHORT).show();
                }
            }

            return true;
        });

        // Afficher le menu
        popupMenu.show();
    }
}
