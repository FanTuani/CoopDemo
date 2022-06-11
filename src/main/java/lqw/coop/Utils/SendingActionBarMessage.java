package lqw.coop.Utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

/**
 * 一个向所有玩家的 ActionBar 栏显示信息的 {@link BukkitRunnable} 实现。
 */
public final class SendingActionBarMessage extends BukkitRunnable {
    /**
     * 将向玩家显示的文本组件。
     */
    private final TextComponent text;
    /**
     * 将看到 <code>text</code> 文本组件的玩家的集合。
     */
    private final Collection<? extends Player> playersToSend;
    /**
     * 此实例的运行次数。
     */
    private int ticked = 0;
    /**
     * 此实例效果的持续时间，以游戏刻为单位。
     */
    private final int ticks;

    /**
     * 只提供 <code>textToSend</code> 的构造方法，所有玩家将会看见目标文本组件。此实例持续时间将会为 20 刻 (1 秒) 。
     *
     * @param textToSend 玩家将看到的文本组件
     */
    public SendingActionBarMessage(@NotNull TextComponent textToSend) {
        this(textToSend, Bukkit.getOnlinePlayers());
    }


    /**
     * 只提供 <code>textToSend</code>, <code>playersToSend</code> 的构造方法。所有玩家将会看见目标文本组件。
     *
     * @param textToSend 玩家将看到的文本组件
     * @param ticks      此实例效果的持续时间，以游戏刻为单位。
     */
    public SendingActionBarMessage(@NotNull TextComponent textToSend, int ticks) {
        this(textToSend, Bukkit.getOnlinePlayers(), ticks);
    }

    public SendingActionBarMessage(@NotNull Player player, @NotNull String textToSend, int ticks) {
        this(new TextComponent(textToSend), Collections.singletonList(player), ticks);
    }

    public SendingActionBarMessage(@NotNull String textToSend, int ticks) {
        this(new TextComponent(textToSend), Bukkit.getOnlinePlayers(), ticks);
    }

    /**
     * 只提供 <code>textToSend</code>, <code>playersToSend</code> 的构造方法。此实例持续时间将会为 20 刻 (1 秒) 。
     *
     * @param textToSend    玩家将看到的文本组件
     * @param playersToSend 将看到目标文本组件的玩家的集合。
     */
    public SendingActionBarMessage(@NotNull TextComponent textToSend, @NotNull Collection<? extends Player> playersToSend) {
        this(textToSend, playersToSend, 20);
    }

    /**
     * 主构造方法。
     *
     * @param textToSend    玩家将看到的文本组件
     * @param playersToSend 将看到目标文本组件的玩家的集合。
     * @param ticks         此实例效果的持续时间，以游戏刻为单位。
     */
    public SendingActionBarMessage(@NotNull TextComponent textToSend, @NotNull Collection<? extends Player> playersToSend, int ticks) {

        Validate.notNull(textToSend, "No text to send :(");
        Validate.notNull(playersToSend, "No players can receive this message :(");
        Validate.isTrue(ticks >= 0, "The ticks cannot be negative or zero.");
        this.text = textToSend;
        this.playersToSend = playersToSend;
        this.ticks = ticks;
    }

    /* 功能的核心实现。 */
    @Override
    public void run() {
        playersToSend.forEach((IT) -> IT.spigot().sendMessage(ChatMessageType.ACTION_BAR, text)); // 2022/2/6 用 Stream 优化。
        if (ticked++ >= ticks) {
            cancel();
        }
    }

    /**
     * 启动此实例。
     */
    public BukkitTask start(Plugin plugin) {
        return super.runTaskTimer(plugin, 0L, 1L);
    }
}