package dev.nymii.legacysmpcore.events;

import dev.nymii.legacysmpcore.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathHandler implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player deadPlayer = event.getEntity();
        Location deathLocation = deadPlayer.getLocation();

        event.deathMessage(null);

        Main.getInstance().getGraveManager().createGrave(deadPlayer, deathLocation);

        Bukkit.getBanList(BanList.Type.PROFILE).addBan(
            deadPlayer.getName(),
            """
            
            §cYour story has come to an end.

            §cDeath is final here, but your legacy remains.
            §cA grave now marks the place where you fell.

            §cThank you for the journey on The LegacySMP.""",
            null,
            null
        );

        deadPlayer.kick(Component.empty());
    }

}

