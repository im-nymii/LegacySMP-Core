package dev.nymii.legacysmpcore.events;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SocialHandler implements Listener {

    @EventHandler
    public void onPlayerSendMessage(AsyncChatEvent event) {
        event.setCancelled(true);
        event.getPlayer().sendMessage(Component.text("The chat is disabled, to talk please use VoiceChat Mod.", NamedTextColor.RED));
    }
}
