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

                // Shoot an arrow at the player
                for (int i = 1; i < 5; i++){
                    Arrow arrow = dragon.launchProjectile(Arrow.class);
                    arrow.setShooter(dragon);

                    Vector modifiedDirection = direction.clone();
                    modifiedDirection.setX(modifiedDirection.getX() + (Math.random() - 0.5) * 0.1);
                    modifiedDirection.setY(modifiedDirection.getY() + (Math.random() - 0.5) * 0.1);
                    modifiedDirection.setZ(modifiedDirection.getZ() + (Math.random() - 0.5) * 0.1);

                    arrow.setVelocity(modifiedDirection.multiply(3));
                }

                // Shoot a dragon fireball
                DragonFireball fireball1 = dragon.launchProjectile(DragonFireball.class);
                fireball1.setDirection(direction);

                Fireball fireball2 = dragon.launchProjectile(Fireball.class);
                fireball2.setDirection(direction);

                // Apply potion effect to nearby players
                for (Entity nearby : dragon.getNearbyEntities(15, 15, 15)) {
                    if (nearby instanceof Player) {
                        ((LivingEntity) nearby).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
                        ((LivingEntity) nearby).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 10, 1));
                    }
                }
            }

        }
    }
}
