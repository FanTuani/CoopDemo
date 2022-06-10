package lqw.coop.Commands;

import lqw.coop.Coop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SetCorner implements CommandExecutor {
    private final Coop plugin = Coop.instance;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] strings) {
        Configuration config = plugin.getConfig();
        if (sender instanceof Player) {
            if (cmd.getName().equalsIgnoreCase("setCorner") && strings.length == 1) {
                Player player = (Player) sender;
                Location playerLocation = player.getLocation();
                List list = new ArrayList();
                list.add((int) playerLocation.getX());
                list.add((int) playerLocation.getZ());
                if (strings[0].equalsIgnoreCase("1") || strings[0].equalsIgnoreCase("2")) {
                    config.set(strings[0], list);
                    plugin.saveConfig();
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "Corner set: " + strings[0]);
                    return true;
                }
            }
        }
        return false;
    }
}
