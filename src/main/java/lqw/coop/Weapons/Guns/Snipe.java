package lqw.coop.Weapons.Guns;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.event.Listener;

public class Snipe extends AbstractGun implements Listener {

    public static double DAMAGE, CAPACITY, SHOOT_SPEED, BULLET_SPEED;

    public Snipe() {
        this.gunItemType = Material.DIAMOND_HOE;
        guns.add(Material.DIAMOND_HOE);
        this.bulletSpeed = 40;
        this.maxRange = 100;
        this.coolDownTicks = 30;
        this.reloadTicks = 60;
        this.damage = 12F;
        this.capacity = 8;
        this.hitBox = 0.25;
        this.particle = Particle.VILLAGER_ANGRY;
        this.shootSound = Sound.ENTITY_DRAGON_FIREBALL_EXPLODE;
        this.recoil = 0.25F;
        this.knockBack = 1.5;
        DAMAGE = damage;
        CAPACITY = capacity;
        SHOOT_SPEED = Math.round(20.0 / coolDownTicks * 100) / 100.0;
        BULLET_SPEED = bulletSpeed;
    }

}
