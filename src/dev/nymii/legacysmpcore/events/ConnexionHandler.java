package dev.nymii.legacysmpcore.events;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnexionHandler implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(null);
        Player player = event.getPlayer();

        player.sendMessage("To enjoy this server, we recommend using VoiceChat Mod");

        player.playerListName(Component.empty());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(null);
    }

}
