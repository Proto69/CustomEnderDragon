package org.example;

import org.bukkit.Sound;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EndCrystalBreakListener implements Listener {
    private final CustomEnderDragon plugin;

    public EndCrystalBreakListener(CustomEnderDragon plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event) {

        // Check if the explosion is caused by an Ender Crystal
        if (event.getEntity() instanceof EnderCrystal enderCrystal) {

            int numberOfCrystalsLeft = event.getEntity().getWorld().getEntitiesByClass(EnderCrystal.class).size();

            // Example: Broadcast to all players
            for (Player player : Objects.requireNonNull(plugin.getServer().getWorld(UsefulMethods.readConfig("end-world-name"))).getPlayers()) {
                if (Objects.equals(UsefulMethods.readConfig("end-crystal-explode.title.enable"), "true")) {

                    String title = UsefulMethods.readConfig("end-crystal-explode.title.title");
                    String subtitle = UsefulMethods.readConfig("end-crystal-explode.title.subtitle");

                    Map<String, String> map = new HashMap<>();
                    map.put("numberOfCrystalsLeft", Integer.toString(numberOfCrystalsLeft));
                    map.put("numberOfCrystalsDestroyed", Integer.toString(10 - numberOfCrystalsLeft));

                    title = UsefulMethods.replacePlaceholders(title, map);
                    subtitle = UsefulMethods.replacePlaceholders(subtitle, map);

                    UsefulMethods.sendTitle(player, title, subtitle);
                }
                if (Objects.equals(UsefulMethods.readConfig("end-crystal-explode.sound.enable"), "true")) {
                    player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, Float.parseFloat(UsefulMethods.readConfig("end-crystal-explode.sound.volume")), Float.parseFloat(UsefulMethods.readConfig("end-crystal-explode.sound.pitch")));
                }
            }
        }
    }
}
