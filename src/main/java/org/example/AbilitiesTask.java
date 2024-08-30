package org.example;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class AbilitiesTask extends BukkitRunnable {

    @Override
    public void run() {
        for (EnderDragon dragon : Objects.requireNonNull(Bukkit.getWorld(UsefulMethods.readConfig("end-world-name"))).getEntitiesByClass(EnderDragon.class)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Location playerLocation = player.getLocation();
                Location dragonLocation = dragon.getLocation();
                Vector direction = playerLocation.toVector().subtract(dragonLocation.toVector()).normalize();

                Boolean chance = UsefulMethods.chance("abilities.arrows.chance");

                // Shoot an arrow at the player
                if (Objects.equals(UsefulMethods.readConfig("abilities.arrows.enable"), "true") && chance){
                    for (int i = 0; i < Integer.parseInt(UsefulMethods.readConfig("abilities.arrows.count")); i++){
                        Arrow arrow = dragon.launchProjectile(Arrow.class);
                        arrow.setShooter(dragon);

                        Vector modifiedDirection = direction.clone();
                        modifiedDirection.setX(modifiedDirection.getX() + (Math.random() - 0.5) * 0.1);
                        modifiedDirection.setY(modifiedDirection.getY() + (Math.random() - 0.5) * 0.1);
                        modifiedDirection.setZ(modifiedDirection.getZ() + (Math.random() - 0.5) * 0.1);

                        arrow.setVelocity(modifiedDirection.multiply(3));
                    }
                }

                chance = UsefulMethods.chance("abilities.fireballs.chance");

                if (Objects.equals(UsefulMethods.readConfig("abilities.fireballs.enable"), "true") && chance){
                    Vector baseDirection = direction.clone();  // Clone the base direction

                    // Adjustments for slight variations in direction
                    Vector[] directions = new Vector[Integer.parseInt(UsefulMethods.readConfig("abilities.fireballs.count"))];
                    for (int i = 0; i < Integer.parseInt(UsefulMethods.readConfig("abilities.fireballs.count")); i++) {
                        directions[i] = baseDirection.clone();
                        // Modify the direction slightly for each fireball
                        directions[i].add(new Vector(
                                (Math.random() - 0.5) * 0.2,  // Small random variation in X
                                (Math.random() - 0.5) * 0.2,  // Small random variation in Y
                                (Math.random() - 0.5) * 0.2   // Small random variation in Z
                        ));
                        directions[i].normalize();  // Ensure the direction vector is normalized
                    }

                    for (Vector dir : directions) {
                        DragonFireball fireball = dragon.launchProjectile(DragonFireball.class);
                        fireball.setDirection(dir);
                    }
                }

                chance = UsefulMethods.chance("abilities.player-effects.chance");

                if (Objects.equals(UsefulMethods.readConfig("abilities.player-effects.enable"), "true") && chance){
                    int radius = Integer.parseInt(UsefulMethods.readConfig("abilities.player-effects.radius"));
                    // Apply potion effect to nearby players
                    for (Entity nearby : dragon.getNearbyEntities(radius, radius, radius)) {
                        if (nearby instanceof Player) {
                            ((LivingEntity) nearby).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
                            ((LivingEntity) nearby).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 10, 1));
                        }
                    }
                }
            }
        }
    }
}
