package org.example;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class UsefulMethods {
    public static String formatColor(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void sendTitle(Player player, String title, String subtitle){
        player.sendTitle(formatColor(title), formatColor(subtitle), Integer.parseInt(readConfig("title.fade-in")), Integer.parseInt(readConfig("title.stay")), Integer.parseInt(readConfig("title.fade-out")));
    }

    public static String readConfig(String path){
        return CustomEnderDragon.config.getString(path);
    }
}
