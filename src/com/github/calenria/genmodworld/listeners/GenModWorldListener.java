package com.github.calenria.genmodworld.listeners;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import com.github.calenria.genmodworld.GenModWorld;
import com.github.calenria.genmodworld.events.WorldGenerateEvent;

public class GenModWorldListener implements Listener {
    /**
     * Bukkit Logger.
     */
    private static Logger log = Logger.getLogger("Minecraft");
    /**
     * NextVote Plugin.
     */
    private GenModWorld plugin = null;

    public GenModWorldListener(final GenModWorld plugin) {

        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
        // log.log(Level.INFO, "Installing database for " +
        // plugin.getDescription().getName() + " due to first time usage");

    }

    /**
     * This is called when your custom event is triggered.
     * 
     * @param event
     *            Event data
     */
    public void onWorldGenerateEvent(WorldGenerateEvent event) {
        Player player = Bukkit.getPlayer(event.getPlayer());
        log.log(Level.INFO, "Teleporting " + player.getName() + " to his world");
        player.teleport(event.getLocation());
        
    }

    /**
     * This is required for your events to be triggered correctly.
     * 
     * @param event
     *            Event data
     */
    public void onCustomEvent(Event event) {
        if (event instanceof WorldGenerateEvent) {
            onWorldGenerateEvent((WorldGenerateEvent) event);
        }
    }

}
