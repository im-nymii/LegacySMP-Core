package dev.nymii.legacysmpcore;

import dev.nymii.legacysmpcore.events.*;
import dev.nymii.legacysmpcore.managers.GraveManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Message {

    private static Main instance;
    private GraveManager graveManager;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("LegacySMP-Core activé !");

        graveManager = new GraveManager(this);

        registerHandlers();
        //registerCommands();
    }

    @Override
    public void onDisable() {
        getLogger().info("LegacySMP-Core désactivé !");
    }
    /**
    private void registerCommands() {
    }
    */

    private void registerHandlers() {

        getServer().getPluginManager().registerEvents(new CommandCatcher(), this);
        getServer().getPluginManager().registerEvents(new ConnexionHandler(), this);
        getServer().getPluginManager().registerEvents(new DeathHandler(), this);
        getServer().getPluginManager().registerEvents(new PvpHandler(), this);
        getServer().getPluginManager().registerEvents(new SocialHandler(), this);
        getServer().getPluginManager().registerEvents(graveManager, this);
    }

    public static Main getInstance() {
        return instance;
    }

    public GraveManager getGraveManager() {
        return graveManager;
    }
}

