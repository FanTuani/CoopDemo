package lqw.coop;

import lqw.coop.Commands.SetCorner;
import lqw.coop.Game.Basics;
import lqw.coop.Game.Game;
import lqw.coop.Game.PropsGiver;
import lqw.coop.Game.RandomRespawn;
import lqw.coop.Guns.AbstractGun;
import lqw.coop.Guns.RPG;
import lqw.coop.Guns.Rifle;
import lqw.coop.Guns.Snipe;
import lqw.coop.Props.Beacon;
import lqw.coop.Props.BlackDye;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

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
//        new WallJump();

        new Rifle();
        new Snipe();
        new RPG();

        new Beacon();
        new BlackDye();

        getCommand("setcorner").setExecutor(new SetCorner());

        for (Player player : getServer().getOnlinePlayers()) {
            player.setLevel(0);
            player.setExp(0);
            player.sendTitle(ChatColor.GREEN + "插件重载完成!", "Yee", 5, 10, 10);
            ItemStack item = player.getInventory().getItemInMainHand();
            if (AbstractGun.guns.contains(item.getType())) {
                Game.setDurability(item, item.getType().getMaxDurability());
            }
        }
    }

    @Override
    public void onDisable() {
        saveDefaultConfig();
        for (Player player : getServer().getOnlinePlayers()) {
            player.sendTitle(ChatColor.RED + "正在重载插件...", "如果你正在换弹，拿着枪别动！", 2, 40, 20);
        }
        // Plugin shutdown logic
    }

    private void initPermeableBlocks(){
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
    }

    private void initProps(){
        Game.props.add(Material.BEACON);
        Game.props.add(Material.BLACK_DYE);
    }
}
