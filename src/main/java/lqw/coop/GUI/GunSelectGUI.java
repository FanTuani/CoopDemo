package lqw.coop.GUI;

import lqw.coop.Coop;
import lqw.coop.Weapons.Guns.AbstractGun;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GunSelectGUI implements Listener {
    public static final Inventory inv;

    public GunSelectGUI() {
        Coop.instance.getServer().getPluginManager().registerEvents(this, Coop.instance);
    }

    static {
        inv = Bukkit.createInventory(null, 9, "Guns");
        for (ItemStack gun : AbstractGun.guns) {
            inv.addItem(gun);
        }
    }

    public static void open(Player player) {
        player.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("Guns")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            if (AbstractGun.guns.contains(player.getInventory().getItemInMainHand())
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
