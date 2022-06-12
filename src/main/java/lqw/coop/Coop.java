package lqw.coop;

import lqw.coop.Commands.SetCorner;
import lqw.coop.Game.Basics;
import lqw.coop.Game.Game;
import lqw.coop.Guns.*;
import lqw.coop.MapInteractions.CampfireRestoreHealth;
import lqw.coop.MapInteractions.PropsGiver;
import lqw.coop.Game.RandomRespawn;
import lqw.coop.Props.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public final class Coop extends JavaPlugin {
    public static Coop instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        initPermeableBlocks();
        initProps();
        getLogger().info("CoopDemo loaded !!!!!!!!!!!!!!!!!!!!!!");

        new RandomRespawn();
        new Basics();

        new PropsGiver();
        new CampfireRestoreHealth();
//        new WallJump();

        new Rifle();
        new Snipe();
//        new RPG();
        new Shotgun();

        getCommand("setcorner").setExecutor(new SetCorner());

        for (Player player : getServer().getOnlinePlayers()) {
            player.setLevel(0);
            player.setExp(0);

            player.sendTitle(ChatColor.GREEN + "插件重载完成!", randomSentence(), 5, 15, 10);
            ItemStack item = player.getInventory().getItemInMainHand();
            if (AbstractGun.guns.contains(item.getType())) {
                Game.setDurability(item, item.getType().getMaxDurability());
            }
        }
    }

    @Override
    public void onDisable() {
        saveDefaultConfig();
        Game.reloadRecover();
        for (Player player : getServer().getOnlinePlayers()) {
            player.sendTitle(ChatColor.RED + "正在重载插件...", "重载结束时会修复你手上物品的耐久", 2, 40, 20);
        }
    }

    private void initPermeableBlocks() {
        Game.permeableBlocks.add(Material.AIR);
        Game.permeableBlocks.add(Material.WATER);
        Game.permeableBlocks.add(Material.LAVA);
        Game.permeableBlocks.add(Material.TALL_GRASS);
        Game.permeableBlocks.add(Material.GRASS_BLOCK);
        Game.permeableBlocks.add(Material.SUNFLOWER);
        Game.permeableBlocks.add(Material.CORNFLOWER);
        Game.permeableBlocks.add(Material.ROSE_BUSH);
        Game.permeableBlocks.add(Material.LANTERN);
        Game.permeableBlocks.add(Material.CHORUS_FLOWER);
        Game.permeableBlocks.add(Material.FLOWER_BANNER_PATTERN);
        Game.permeableBlocks.add(Material.FLOWER_POT);
        Game.permeableBlocks.add(Material.SCAFFOLDING);
        Game.permeableBlocks.add(Material.SUGAR_CANE);
        Game.permeableBlocks.add(Material.LADDER);
        Game.permeableBlocks.add(Material.VINE);
        Game.permeableBlocks.add(Material.STONE_BUTTON);
        Game.permeableBlocks.add(Material.OAK_TRAPDOOR);
        Game.permeableBlocks.add(Material.DANDELION);
        Game.permeableBlocks.add(Material.OXEYE_DAISY);
        Game.permeableBlocks.add(Material.POTTED_OXEYE_DAISY);
        Game.permeableBlocks.add(Material.DAYLIGHT_DETECTOR);
        Game.permeableBlocks.add(Material.LILAC);
        Game.permeableBlocks.add(Material.BRAIN_CORAL_BLOCK);
        Game.permeableBlocks.add(Material.DARK_OAK_FENCE);
        Game.permeableBlocks.add(Material.DARK_OAK_FENCE_GATE);
        Game.permeableBlocks.add(Material.OAK_FENCE);
        Game.permeableBlocks.add(Material.OAK_FENCE_GATE);
        Game.permeableBlocks.add(Material.BROWN_CARPET);
    }

    private void initProps() {
        new Beacon();
        new BedRock();
        new Glass();
        new Firework();
        new CreeperHead();
        new BoneMeal();
    }

    private String randomSentence() {
        String string = "";
        switch (new Random().nextInt(7)) {
            case 0:
                string = "null（雾";
                break;
            case 1:
                string = "奇怪的bug增加了！";
                break;
            case 2:
                string = "这次有没有修复问题呢...";
                break;
            case 3:
                string = "此次重载耗时11451ms（";
                break;
            case 4:
                string = "hmmm其实这条消息是随机的";
                break;
            case 5:
                string = "我想我找到了解决方法！（报错";
                break;
            case 6:
                string = "移除了him（认真";
                break;
        }
        return string;
    }
}
