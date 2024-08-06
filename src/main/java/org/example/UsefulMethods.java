package org.example;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;

public class UsefulMethods {
    public static String formatColor(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void sendTitle(Player player, String title, String subtitle){
        player.sendTitle(formatColor(title), formatColor(subtitle), Integer.parseInt(readConfig("all-titles.fade-in")), Integer.parseInt(readConfig("all-titles.stay")), Integer.parseInt(readConfig("all-titles.fade-out")));
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
