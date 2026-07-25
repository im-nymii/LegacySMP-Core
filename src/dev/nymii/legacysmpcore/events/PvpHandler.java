package dev.nymii.legacysmpcore.events;

import io.papermc.paper.event.entity.TameableDeathMessageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PvpHandler implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setShowDeathMessages(false);
    }

    @EventHandler
    public void onTameableDeathMessage(TameableDeathMessageEvent event) {
        event.setCancelled(true);
    }
}
