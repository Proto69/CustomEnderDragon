package org.example;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PluginTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        Player player = null;
        if (sender instanceof Player) player = (Player) sender;

        if (command.getName().equalsIgnoreCase("ced")) {
            if (args.length == 1) {
                if (player == null) {
                    suggestions.add("reload");
                } else {
                    if (player.hasPermission("ced.reload")) {
                        suggestions.add("reload");
                    }
                }
            }
        }
        return suggestions;
    }
}