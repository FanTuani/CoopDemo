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
    private static final HashMap<UUID, Boolean> ifReloading = new HashMap<>();

    public static void initMaps() {
        for (Player player : Coop.instance.getServer().getOnlinePlayers()) {
            Game.setIsCooledOver(player, true);
            Game.setIsReloading(player, false);
        }
    }

    public static boolean getIsCooledOver(Player player) {
        UUID uuid = player.getUniqueId();
        return ifCooledOver.getOrDefault(uuid, false);
    }

    public static void setIsCooledOver(Player player, boolean isCooled) {
        ifCooledOver.put(player.getUniqueId(), isCooled);
    }

    public static boolean reduceCapacity(ItemStack item, int capacity) {
        int maxDur = item.getType().getMaxDurability();
        if (getDurability(item) <= maxDur / capacity) {
            return false;
        } else {
            setDurability(item, getDurability(item) - maxDur / capacity);
            return true;
        }
    }

    public static int getDurability(ItemStack item) {
        return item.getType().getMaxDurability() - ((Damageable) item.getItemMeta()).getDamage();
    }

    public static void setDurability(ItemStack item, int durability) {
        Damageable dmg = (Damageable) item.getItemMeta();
        dmg.setDamage(item.getType().getMaxDurability() - durability);
        // getDamage 返回值为 <损坏度> 而非 <耐久度>
        // <损坏度> == 物品最大耐久 - <耐久度>
        ItemMeta meta = (ItemMeta) dmg;
        item.setItemMeta(meta);
    }

    public static boolean getIsReloading(Player player) {
        UUID uuid = player.getUniqueId();
        return ifReloading.getOrDefault(uuid, false);
    }

    public static void setIsReloading(Player player, boolean isReloading) {
        ifReloading.put(player.getUniqueId(), isReloading);
    }
}
