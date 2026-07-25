package dev.nymii.legacysmpcore;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public interface Message {

    MiniMessage MM = MiniMessage.miniMessage();

    default void send(CommandSender sender, String message) {
        sender.sendMessage(MM.deserialize(message));
    }

    default Component parse(String message) {
        return MM.deserialize(message);
    }

    default Component prefix(String message) {
        return MM.deserialize("<dark_gray>[<gradient:#FFD700:#FFA500>LegacySMP</gradient><dark_gray>] <gray>" + message);
    }
}

