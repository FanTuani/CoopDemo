package lqw.coop.Game;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;

public class Game {
    public static final HashSet<Material> permeableBlocks = new HashSet<>();
    public static final HashSet<Material> props = new HashSet<>();

    public static boolean getIsCooledOver(Player player) {
        return player.getExp() == 1F;
    }

    public static boolean isPermeable(Material material) {
        return permeableBlocks.contains(material);
    }

    public static int getDurability(ItemStack item) {
        if (item.getItemMeta() == null) return 0;
        return item.getType().getMaxDurability() - ((Damageable) item.getItemMeta()).getDamage();
    }

    public static void setDurability(ItemStack item, int durability) {
        if (item == null || item.getItemMeta() == null) return;
        Damageable dmg = (Damageable) item.getItemMeta();
        dmg.setDamage(item.getType().getMaxDurability() - durability);
        // getDamage 返回值为 <损坏度> 而非 <耐久度>
        // <损坏度> == 物品最大耐久 - <耐久度>
        ItemMeta meta = (ItemMeta) dmg;
        item.setItemMeta(meta);
    }

    public static boolean getIsReloading(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        return getDurability(item) != item.getType().getMaxDurability();
    }
}
