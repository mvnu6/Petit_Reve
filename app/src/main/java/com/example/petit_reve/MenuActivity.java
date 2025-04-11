package com.example.petit_reve;


import android.content.Context;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;
public class MenuActivity {
    public static void showMenu(Context context, View v) {
        // Créer un PopupMenu
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.getMenu().add("Activer le son");
        popupMenu.getMenu().add("Désactiver le son");

        // Définir un listener pour les éléments du menu
        popupMenu.setOnMenuItemClickListener(item -> {
            String selectedOption = item.getTitle().toString();
            Toast.makeText(context, selectedOption + " sélectionnée", Toast.LENGTH_SHORT).show();
            return true;
        });

        // Afficher le menu
        popupMenu.show();
    }
}

