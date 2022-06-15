package lqw.coop.MapInteractions;

import lqw.coop.Coop;
import lqw.coop.Game.Game;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class CampfireRestoreHealth implements Listener {
    public final Coop plugin = Coop.instance;

    public CampfireRestoreHealth() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerRightClickCampfire(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.CAMPFIRE) {
                Player player = event.getPlayer();
                Game.delayRecoverBlocks(event.getClickedBlock().getLocation(), Material.CAMPFIRE, Material.BIRCH_WOOD, 20 * 30);
                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 1));
                if (player.getPotionEffect(PotionEffectType.SPEED) == null)
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 0));
                else
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, player.getPotionEffect(PotionEffectType.SPEED).getAmplifier() + 1));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.getPotionEffect(PotionEffectType.REGENERATION) == null) cancel();
                        Location parLoc = player.getLocation();
                        parLoc.setY(parLoc.getY() + 2);
                        player.getWorld().spawnParticle(Particle.HEART, parLoc, 1);
                    }
                }.runTaskTimer(plugin, 0, 4);
            }
        }
    }
}
