package lqw.coop.Game;

import lqw.coop.Coop;
import lqw.coop.Weapons.Guns.AbstractGun;
import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class DieRandomRespawn implements Listener {
    private final Coop plugin = Coop.instance;

    public DieRandomRespawn() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDie(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getFinalDamage() >= ((Player) event.getEntity()).getHealth()) {
            event.setCancelled(true);
            Player player = (Player) event.getEntity();
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 21, 2));
            player.removePotionEffect(PotionEffectType.SPEED);
            PlayerInventory inventory = player.getInventory();
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack item = inventory.getItem(i);
                if (item != null) {
                    Game.setDurability(item, item.getType().getMaxDurability());
                    if (!AbstractGun.guns.contains(item)) {
                        inventory.clear(i);
                    }
                }
            }

            player.getWorld().spawnParticle(Particle.LAVA, player.getLocation(), 50, 0, 0, 0, 10);
            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 0.1F);
            player.setGameMode(GameMode.SPECTATOR); // 切模式 给音效 抬高高
            Vector vector = player.getVelocity();
            vector.setY(vector.getY() + 3);
            player.setVelocity(vector);

            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                Game.sendTitle2All(ChatColor.YELLOW + player.getName() + " 选择了摆烂",
                        "他把自己摔死了", 2, 20, 2);
            } else if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                Game.sendTitle2All(ChatColor.YELLOW + player.getName() + " 可能是个资本家",
                        "他被火烧死了", 2, 20, 2);
            } else if (event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                Game.sendTitle2All(ChatColor.YELLOW + player.getName() + " 试图在岩浆里游泳",
                        "这泳可不兴游啊", 2, 20, 2);
            } else if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                Game.sendTitle2All(ChatColor.YELLOW + player.getName() + " 干脆被拳头锤死了",
                        "谁还记得我们有枪？", 2, 20, 2);
            } else if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                Game.sendTitle2All(ChatColor.YELLOW + player.getName() + " 爆炸了",
                        "Creeper?", 2, 20, 2);
            } else {
                Game.sendTitle2All(ChatColor.YELLOW + player.getName() + " 寄了",
                        "总之就是寄了(", 2, 20, 2);
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.setGameMode(GameMode.ADVENTURE);
                    player.setHealth(20);
                    Configuration config = plugin.getConfig();
                    List list = config.getList("min");
                    int x1 = new Integer(list.get(0).toString());
                    int x2 = new Integer(list.get(1).toString());
                    list = config.getList("max");
                    int z1 = new Integer(list.get(0).toString());
                    int z2 = new Integer(list.get(1).toString());
                    Location loc = player.getLocation();
                    int r1 = new Random().nextInt(Math.abs(x1 - z1)) + x1;
                    int r2 = new Random().nextInt(Math.abs(x2 - z2)) + x2;
                    loc.setX(r1);
                    loc.setZ(r2);
                    int y = 60;
                    while (new Location(player.getWorld(), r1, y, r2).getBlock().getType() == Material.AIR) {
                        y--;
                    }
                    loc.setY(y + 3);
                    player.teleport(loc);
                }
            }.runTaskLater(plugin, 25);
        }
    }
}
