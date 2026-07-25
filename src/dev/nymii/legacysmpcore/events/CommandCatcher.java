package dev.nymii.legacysmpcore.events;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.Collections;

public class CommandCatcher implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCommandEvent(PlayerCommandPreprocessEvent event) {
        event.setCancelled(true);
        event.getPlayer().sendMessage(Component.text("Commands are disabled.", NamedTextColor.RED));
    }

    @EventHandler
    public void onCommandSend(PlayerCommandSendEvent event) {
        event.getCommands().clear();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTabComplete(AsyncTabCompleteEvent event) {
        event.setCompletions(Collections.emptyList());
        event.setHandled(true);
    }
}
