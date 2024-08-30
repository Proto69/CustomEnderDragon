package org.example;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Random;

public class UsefulMethods {
    public static String formatColor(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void sendTitle(Player player, String title, String subtitle){
        player.sendTitle(formatColor(title), formatColor(subtitle), Integer.parseInt(readConfig("all-titles.fade-in")), Integer.parseInt(readConfig("all-titles.stay")), Integer.parseInt(readConfig("all-titles.fade-out")));
    }

    public static Boolean chance(String path){
        Random random = new Random();
        int randomNumber = random.nextInt(100);

        // Return true if the random number is less than the chance percentage
        return randomNumber < Integer.parseInt(readConfig(path));
    }

    public static String readConfig(String path){
        return CustomEnderDragon.config.getString(path);
    }

    // Replacing the placeholders like %nickname% with their values
    public static String replacePlaceholders(String template, Map<String, String> variables) {
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            template = template.replace("%" + entry.getKey() + "%", entry.getValue());
        }
        return template;
    }
}
