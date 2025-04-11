package com.example.petit_reve;

import android.content.Context;
import android.media.AudioManager;
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

            // Récupérer le service AudioManager pour manipuler le volume
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            // Vérifier l'état actuel du mode sonnerie du téléphone
            int ringerMode = audioManager.getRingerMode();  // Mode sonore du téléphone
            boolean isSilent = ringerMode == AudioManager.RINGER_MODE_SILENT; // Si le téléphone est en mode silencieux
            boolean isVibrate = ringerMode == AudioManager.RINGER_MODE_VIBRATE; // Si le téléphone est en mode vibreur
            boolean isNormal = ringerMode == AudioManager.RINGER_MODE_NORMAL; // Si le téléphone est en mode normal

            if (selectedOption.equals("Activer le son")) {
                // Si le téléphone est en mode silencieux ou vibreur
                if (isSilent || isVibrate) {
                    try {
                        // Passer en mode normal
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL); // Passer en mode normal
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

                        // Mise à jour de l'état du son
                        isSoundEnabled = true;

                        // Afficher un message pour informer l'utilisateur que le mode silencieux a été désactivé
                        Toast.makeText(context, "Le mode silencieux/vibreur a été désactivé. Son activé.", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        // Gérer l'exception si le mode sonore ne peut pas être changé
                        Toast.makeText(context, "Impossible de changer le mode sonore. Assurez-vous que l'appareil n'est pas bloqué.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Si le mode normal est déjà actif et le son est déjà activé
                    if (!isSoundEnabled) {
                        isSoundEnabled = true;
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                        Toast.makeText(context, "Son activé", Toast.LENGTH_SHORT).show();
                    } else {
                        // Le téléphone est déjà en mode normal, donc le son est déjà activé
                        Toast.makeText(context, "Le son est déjà activé", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (selectedOption.equals("Désactiver le son")) {
                if (isSoundEnabled) {
                    isSoundEnabled = false;

                    // Désactiver le volume
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
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
