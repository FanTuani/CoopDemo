package lqw.coop.Guns;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.event.Listener;

public class Rifle extends AbstractGun implements Listener {

    public Rifle() {
        this.gunItemType = Material.STONE_HOE;
        AbstractGun.guns.add(Material.STONE_HOE);
        this.bulletSpeed = 15;
        this.maxRange = 60;
        this.coolDownTicks = 4;
        this.reloadTicks = 20;
        this.damage = 2F;
        this.capacity = 26;
        this.hitBox = 0.25;
        this.particle = Particle.FIREWORKS_SPARK;
        this.shootSound = Sound.BLOCK_BAMBOO_HIT;
        this.recoil = 0.05F;
        this.knockBack = 0.1F;
    }

}
