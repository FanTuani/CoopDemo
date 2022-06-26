package lqw.coop.Game;

import lqw.coop.Coop;
import lqw.coop.Weapons.Guns.AbstractGun;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Basics implements Listener {
    private final Coop plugin = Coop.instance;

    public Basics() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (int i = 0; i < 2; i++) {
            event.getPlayer().getInventory().addItem(AbstractGun.guns.get(i));
        }
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            player.sendTitle(ChatColor.GREEN + event.getPlayer().getName() + " 来了！", "", 5, 20, 5);
        }
        sendTutorialMsg(event.getPlayer());
    }

    private void sendTutorialMsg(Player player) {
        player.sendMessage(ChatColor.BLUE + "---------- GameRules ----------");
        player.sendMessage(ChatColor.YELLOW + "右键开火，交换主副手键换弹");
        player.sendMessage(ChatColor.YELLOW + "剩余弹药量和射击间隔会显示在经验条上");
        player.sendMessage(ChatColor.YELLOW + "踩在金块上可以获取随机道具并右键使用");
        player.sendMessage(ChatColor.YELLOW + "右键营火可以回血");
        player.sendMessage(ChatColor.YELLOW + "使用 /guns 切换枪械（记得切换后先换弹）");
        player.sendMessage(ChatColor.BLUE + "-------------------------------");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        for (Player player : Coop.instance.getServer().getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.1F, 0.1F);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.getPlayer().getInventory().clear();
    }

    @EventHandler
    public void noBlockPut(BlockPlaceEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerGetExp(PlayerExpChangeEvent event) {
        event.setAmount(0);
    }

    @EventHandler
    public void onPlayerHungry(FoodLevelChangeEvent event) {
        event.getEntity().setFoodLevel(20);
        event.setCancelled(true);
    }

    @EventHandler
    public void noBlockBreaking(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) event.setCancelled(true);
    }

}
