package lqw.coop.Commands;

import lqw.coop.GUI.GunSelectGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GunsChanger implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (command.getName().equalsIgnoreCase("guns")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                GunSelectGUI.open(player);
            }
        }
        return true;
    }
}
