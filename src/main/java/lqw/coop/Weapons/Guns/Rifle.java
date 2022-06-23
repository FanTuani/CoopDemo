package lqw.coop.Weapons.Guns;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Rifle extends AbstractGun implements Listener {


    public Rifle() {
        this.gunItemType = Material.STONE_HOE;
        this.bulletSpeed = 15;
        this.maxRange = 60;
        this.coolDownTicks = 4;
        this.reloadTicks = 30;
        this.damage = 2F;
        this.capacity = 26;
        this.hitBox = 0.25;
        this.particle = Particle.FIREWORKS_SPARK;
        this.shootSound = Sound.BLOCK_BAMBOO_HIT;
        this.recoil = 0.05F;
        this.knockBack = 0.1F;
        initGunItem();
    }

    protected void initGunItem() {
        ItemStack item = new ItemStack(gunItemType, 1);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("步枪");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "伤害: " + damage);
        lore.add(ChatColor.GRAY + "弹夹容量: " + capacity);
        lore.add(ChatColor.GRAY + "弹道速度: " + bulletSpeed);
        lore.add(ChatColor.GRAY + "每秒射击数: " + Math.round(20.0 / coolDownTicks * 100) / 100.0);
        meta.setLore(lore);
        item.setItemMeta(meta);
        AbstractGun.guns.add(item);
    }
}
