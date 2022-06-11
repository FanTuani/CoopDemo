package lqw.coop.Game;

import lqw.coop.Coop;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
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

    public static void sendTitle2All(String mainStr, String offStr, int intro, int during, int outro) {
        for (Player player : Coop.instance.getServer().getOnlinePlayers()) {
            player.sendTitle(mainStr, offStr, intro, during, outro);
        }
    }

    public static void scoredPlus(Player player) {
        Configuration config = Coop.instance.getConfig();
        if (!config.contains(player.getName())) config.set(player.getName(), 1);
        else {
            int s = config.getInt(player.getName()) + 1;
            config.set(player.getName(), s);
        }
        Coop.instance.saveConfig();

        Scoreboard scoreboard = Coop.instance.getServer().getScoreboardManager().getMainScoreboard();
        Objective obj;
        if (scoreboard.getObjective(player.getName()) == null)
            obj = scoreboard.registerNewObjective(player.getName(), "dummy", "申必排行榜");
        else
            obj = scoreboard.getObjective(player.getName());
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.getScore(player.getName()).setScore(config.getInt(player.getName()));
        player.setScoreboard(scoreboard);
    }

    public static HashMap<Location, Material> matMap = new HashMap<>();

    public static void delayRecoverBlocks(Location loc, Material bfMat, Material afMat, int delay) {
        loc.getBlock().setType(afMat);
        matMap.put(loc, bfMat);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (loc.getBlock().getType() == afMat) { // 防某些lry用
                    matMap.remove(loc);
                    loc.getBlock().setType(bfMat);
                    loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                }
            }
        }.runTaskLater(Coop.instance, delay);
    }

    public static void reloadRecover() {
        for (Location location : matMap.keySet()) {
            location.getBlock().setType(matMap.get(location));
        }
    }
}
