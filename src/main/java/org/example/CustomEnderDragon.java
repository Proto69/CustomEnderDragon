package org.example;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.module.Configuration;
import java.util.Objects;

import static org.bukkit.Bukkit.getScheduler;

public class CustomEnderDragon extends JavaPlugin {
    public static FileConfiguration config;
    @Override
    public void onEnable() {
        // Copy the config.yml in the plugin configuration folder if it doesn't exist.
        saveDefaultConfig();

        config = this.getConfig();

        boolean enable = getConfig().getBoolean("enable");

        if (enable) {
            // Registering the events, tasks and commands
            registerEvents();
            registerCommands();
            registerTasks();

            // Logging the successful enabling of the plugin
            getLogger().info("CustomEnderDragon is enabled!");
        } else {
            getLogger().warning("CustomEnderDragon is not enabled in the config!");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ced")) {
            if (args.length == 0) {
                sender.sendMessage("Usage: /ced <subcommand>");
                return true;
            }

            String subCommand = args[0].toLowerCase();

            switch (subCommand) {
                case "reload":
                    ReloadCommand reload = new ReloadCommand(this);
                    reload.onCommand(sender, command, label, args);
                    break;
                default:
                    sender.sendMessage("Unknown subcommand.");
                    break;
            }
            return true;
        }

        return false;
    }

    // Reloads the plugin
    public void reload() {
        // Reloads the configuration
        reloadConfig();

        config = this.getConfig();

        // Unregisters all events and tasks
        HandlerList.unregisterAll(this);
        getScheduler().cancelTasks(this);

        if (getConfig().getBoolean("enable"))
        {
            // Registers all events and tasks
            registerEvents();
            registerTasks();
        }

    }

    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getServer().getPluginManager().registerEvents(new EndCrystalBreakListener(this), this);
    }

    private void registerCommands() {
        // Registers the reload command
        Objects.requireNonNull(this.getCommand("ced")).setExecutor(this);
        Objects.requireNonNull(this.getCommand("ced")).setTabCompleter(new PluginTabCompleter());
    }

    private void registerTasks(){
        new AbilitiesTask().runTaskTimer(this, 0L, 100L);
    }
}