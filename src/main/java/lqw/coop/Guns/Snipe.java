package lqw.coop.Guns;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class Snipe extends AbstractGun{
    public Snipe() {
        this.gunItemType = Material.DIAMOND_HOE;
        this.bulletSpeed = 20;
        this.maxRange = 100;
        this.coolDownTicks = 30;
        this.reloadTicks = 60;
        this.damage = 5F;
        this.capacity = 15;
        this.hitBox = 0.25;
        this.particle = Particle.VILLAGER_ANGRY;
        this.shootSound = Sound.ENTITY_DRAGON_FIREBALL_EXPLODE;

        AbstractGun.guns.add(Material.DIAMOND_HOE);
    }

}
