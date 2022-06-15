package lqw.coop.GUI;

import lqw.coop.Coop;
import lqw.coop.Weapons.Guns.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GunSelectGUI implements Listener {
    public static final Inventory inv;

    public GunSelectGUI() {
        Coop.instance.getServer().getPluginManager().registerEvents(this, Coop.instance);
    }

    static {
        inv = Bukkit.createInventory(null, 9, "Guns");
        int idx = 0;

        ItemStack rifle = getEnchantGun(Material.STONE_HOE);
        inv.setItem(idx++, rifle);

        ItemStack snipe = getEnchantGun(Material.DIAMOND_HOE);
        inv.setItem(idx++, snipe);

        ItemStack shotgun = getEnchantGun(Material.IRON_HOE);
        inv.setItem(idx++, shotgun);

        ItemStack rpg = getEnchantGun(Material.DIAMOND_SHOVEL);
        inv.setItem(idx++, rpg);
    }

    public static ItemStack getEnchantGun(Material material) {
        ItemStack item = new ItemStack(material, 1);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        ItemMeta meta = item.getItemMeta();
        switch (material) {
            case STONE_HOE: {
                meta.setDisplayName("步枪");
                meta.setLore(getRifleLore());
                item.setItemMeta(meta);
                break;
            }
            case DIAMOND_HOE: {
                meta.setDisplayName("大狙");
                meta.setLore(getSnipeLore());
                item.setItemMeta(meta);
                break;
            }
            case IRON_HOE: {
                meta.setDisplayName("霰弹枪");
                meta.setLore(getShotgunLore());
                item.setItemMeta(meta);
                break;
            }
            case DIAMOND_SHOVEL: {
                meta.setDisplayName("RPG");
                meta.setLore(getRPGLore());
                item.setItemMeta(meta);
                break;
            }
        }
        return item;
    }

    private static List<String> getRifleLore() {
        List<String> retLore = new ArrayList();
        retLore.add(ChatColor.GRAY + "伤害: " + Rifle.DAMAGE);
        retLore.add(ChatColor.GRAY + "弹夹容量: " + Rifle.CAPACITY);
        retLore.add(ChatColor.GRAY + "弹道速度: " + Rifle.BULLET_SPEED);
        retLore.add(ChatColor.GRAY + "每秒射击数: " + Rifle.SHOOT_SPEED);
        return retLore;
    }

    private static List<String> getSnipeLore() {
        List<String> retLore = new ArrayList();
        retLore.add(ChatColor.GRAY + "伤害: " + Snipe.DAMAGE);
        retLore.add(ChatColor.GRAY + "弹夹容量: " + Snipe.CAPACITY);
        retLore.add(ChatColor.GRAY + "弹道速度: " + Snipe.BULLET_SPEED);
        retLore.add(ChatColor.GRAY + "每秒射击数: " + Snipe.SHOOT_SPEED);
        return retLore;
    }

    private static List<String> getShotgunLore() {
        List<String> retLore = new ArrayList();
        retLore.add(ChatColor.GRAY + "单子弹伤害: " + Shotgun.DAMAGE);
        retLore.add(ChatColor.GRAY + "弹夹容量: " + Shotgun.CAPACITY);
        retLore.add(ChatColor.GRAY + "弹道速度: " + Shotgun.BULLET_SPEED);
        retLore.add(ChatColor.GRAY + "每秒射击数: Infinity");
        return retLore;
    }

    private static List<String> getRPGLore() {
        List<String> retLore = new ArrayList();
        retLore.add(ChatColor.GRAY + "伤害: 爆炸");
        retLore.add(ChatColor.GRAY + "弹夹容量: " + RPG.CAPACITY);
        retLore.add(ChatColor.GRAY + "弹道速度: " + RPG.BULLET_SPEED + "++");
        return retLore;
    }


    public static void open(Player player) {
        player.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("Guns")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            if (AbstractGun.guns.contains(player.getInventory().getItemInMainHand().getType())
                    && !player.getInventory().contains(event.getCurrentItem())) {
                player.getInventory().setItem(EquipmentSlot.HAND, event.getCurrentItem());
                player.sendMessage(ChatColor.LIGHT_PURPLE + "更换枪械: " + event.getCurrentItem().getItemMeta().getDisplayName());
                player.setLevel(0);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 0.2F, 2);
            } else {
                player.sendMessage(ChatColor.RED + "必须手持一把用于更换的枪!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.2F, 1);
            }
            player.closeInventory();
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("Guns")) {
            event.setCancelled(true);
        }
    }
}
