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

public class Snipe extends AbstractGun implements Listener {


    public Snipe() {
        this.gunItemType = Material.DIAMOND_HOE;
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
        initGunItem();
    }

    @Override
    protected void initGunItem() {
        ItemStack item = new ItemStack(gunItemType, 1);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("大狙");
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
