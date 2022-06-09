package lqw.coop.Game;

import lqw.coop.Coop;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class Game {
    private static final HashMap<UUID, Boolean> ifCooledOver = new HashMap<>();

    public static void initMaps() {
//        for (Player player : Coop.instance.getServer().getOnlinePlayers()) {
//            Game.setIsCooledOver(player, true);
//            Game.setIsReloading(player, false);
//        }
    }

    public static boolean getIsCooledOver(Player player) {
        return player.getExp() == 1F;
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
