package org.example;

import org.bukkit.Sound;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.util.Objects;

public class EndCrystalBreakListener implements Listener {
    private final CustomEnderDragon plugin;

    public EndCrystalBreakListener(CustomEnderDragon plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event) {

        if (Objects.equals(UsefulMethods.readConfig("end-crystal-explode-title.enabled"), "true")) {
            // Check if the explosion is caused by an Ender Crystal
            if (event.getEntity() instanceof EnderCrystal enderCrystal) {

                // Example: Broadcast to all players
                for (Player player : Objects.requireNonNull(plugin.getServer().getWorld(UsefulMethods.readConfig("end-world-name"))).getPlayers()) {
                    UsefulMethods.sendTitle(player, UsefulMethods.readConfig("end-crystal-explode-title.title"), UsefulMethods.readConfig("end-crystal-explode-title.subtitle"));
                    player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1.0f, 1.0f);
                }
            }
        }
    }
}
