package org.example;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.DragonFireball;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Particle;
import org.bukkit.Sound;

import java.util.*;

public class DamageListener implements Listener {
    private final Map<UUID, Double> damageMap = new HashMap<>();
    private Integer buffStage = 0;

    // Cancels bed damage if enabled in config.yml
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {

        // Checks if the event is the correct one
        if (event.getEntity().getType() == EntityType.ENDER_DRAGON && event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {

            // If enabled, cancels the event, so no damage
            if (Objects.equals(UsefulMethods.readConfig("no-bed-damage"), "true")){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntityType() == EntityType.ENDER_DRAGON) {
            EnderDragon dragon = (EnderDragon) event.getEntity();

            Player player = null;

            // Check if the attacker is a Player (melee attack)
            if (event.getDamager() instanceof Player) {
                player = (Player) event.getDamager();
            }
            // Check if the attacker is an Arrow (ranged attack)
            else if (event.getDamager() instanceof Arrow arrow) {
                if (arrow.getShooter() instanceof Player) {
                    player = (Player) arrow.getShooter();
                }
            }

            // Buffs the dragon when falling below certain health
            if (Objects.equals(UsefulMethods.readConfig("buff-stages"), "true")) {
                if (buffStage < 3 && dragon.getHealth() < (0.25 * Objects.requireNonNull(dragon.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue())) {
                    buffs(dragon, 3, 3);
                    buffStage = 3;
                } else if (buffStage < 2 && dragon.getHealth() < (0.5 * Objects.requireNonNull(dragon.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue())) {
                    buffs(dragon, 2, 2);
                    buffStage = 2;
                } else if (buffStage < 1 && dragon.getHealth() < (0.75 * Objects.requireNonNull(dragon.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue())) {
                    buffs(dragon, 1, 1);
                    buffStage = 1;
                }
            }

            // If the player is not null, proceed with applying effects
            if (player != null) {

                damageMap.merge(player.getUniqueId(), event.getDamage(), Double::sum);

                Random random = new Random();

                if (Objects.equals(UsefulMethods.readConfig("dragon-effects"), "true")) {
                    if (random.nextBoolean()) {
                        // Apply potion effects to the dragon
                        if (!dragon.hasPotionEffect(PotionEffectType.REGENERATION)) {
                            dragon.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 3));
                            dragon.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 40, 4));
                        }
                    }
                }

                if (Objects.equals(UsefulMethods.readConfig("player-effects"), "true")) {
                    if (random.nextBoolean()) {
                        // Apply potion effects to the player
                        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 60, 1));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1));
                    }
                }

                if (Objects.equals(UsefulMethods.readConfig("dragon-fireball-damage"), "true")) {
                    if (random.nextBoolean()) {
                        // Shoot a dragon fireball
                        dragon.launchProjectile(DragonFireball.class);
                    }
                }

                if (Objects.equals(UsefulMethods.readConfig("dragon-particles"), "true")) {
                    // Spawn red particles around the dragon
                    dragon.getWorld().spawnParticle(Particle.DUST, dragon.getLocation(), 250, 1, 1, 1, 0, new Particle.DustOptions(org.bukkit.Color.RED, 1.0f));
                }

                if (Objects.equals(UsefulMethods.readConfig("player-particles"), "true")) {
                    dragon.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 50, 1, 1, 1);
                }

                if (Objects.equals(UsefulMethods.readConfig("player-sound-damage"), "true")) {
                    // Play a sound effect when the dragon takes damage
                    dragon.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    dragon.getWorld().playSound(player.getLocation(), Sound.ENTITY_RAVAGER_HURT, 1.0f, 1.0f);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (Objects.equals(UsefulMethods.readConfig("show-damage-statistics"), "true")) {
            if (event.getEntity() instanceof EnderDragon) {
                displayTopAttackers();
                damageMap.clear(); // Clear the map for the next dragon fight
            }
        }
    }

    private void displayTopAttackers() {
        List<Map.Entry<UUID, Double>> sortedDamageList = new ArrayList<>(damageMap.entrySet());
        sortedDamageList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        Bukkit.broadcastMessage(UsefulMethods.readConfig("leaderboard-message"));
        for (int i = 0; i < Math.min(Integer.parseInt(UsefulMethods.readConfig("leaderboard-player-count")), sortedDamageList.size()); i++) {
            Map.Entry<UUID, Double> entry = sortedDamageList.get(i);
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null) {
                // TODO: placeholder text
                // TODO: %number. %player dealt %damage damage to the dragon!
                Bukkit.broadcastMessage((i + 1) + ". " + player.getName() + ": " + Math.round(entry.getValue()) + " damage");
            }
        }
    }
    private void buffs(EnderDragon dragon, Integer speedMultiplier, Integer damageMultiplier){
        if (dragon.hasPotionEffect(PotionEffectType.SPEED)){
            dragon.removePotionEffect(PotionEffectType.SPEED);
        }
        dragon.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speedMultiplier));

        if (dragon.hasPotionEffect(PotionEffectType.STRENGTH)){
            dragon.removePotionEffect(PotionEffectType.STRENGTH);
        }
        dragon.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, damageMultiplier));

        if (Objects.equals(UsefulMethods.readConfig("dragon-buff-title.enabled"), "true")) {
            for (Player player : Objects.requireNonNull(Bukkit.getWorld(UsefulMethods.readConfig("end-world-name"))).getPlayers()) {
                UsefulMethods.sendTitle(player, UsefulMethods.readConfig("dragon-buff-title.title"), UsefulMethods.readConfig("dragon-buff-title.title"));
            }
        }
    }
}
