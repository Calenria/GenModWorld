package com.github.calenria.genmodworld.events;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WorldGenerateEvent extends Event implements Cancellable {
    private String player;
    private String world;
    private Location loc;
    private boolean cancelled;

    public WorldGenerateEvent(String world, String player, Location loc) {
        super();
        this.player = player;
        this.world = world;
        this.loc = loc;
    }

    public Location getLocation() {
        return this.loc;
    }

    public void setLocation(Location loc) {
        this.loc = loc;
    }

    public String getPlayer() {
        return this.player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;

    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}