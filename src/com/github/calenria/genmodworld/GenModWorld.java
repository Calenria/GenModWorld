/*
 * Copyright (C) 2012 Calenria <https://github.com/Calenria/> and contributors
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3.0 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package com.github.calenria.genmodworld;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.calenria.genmodworld.commands.GenModWorldCommands;
import com.github.calenria.genmodworld.models.ConfigData;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import com.sk89q.minecraft.util.commands.CommandsManager;
import com.sk89q.minecraft.util.commands.MissingNestedCommandException;
import com.sk89q.minecraft.util.commands.SimpleInjector;
import com.sk89q.minecraft.util.commands.WrappedCommandException;

/**
 * SimpleChat ein BukkitPlugin zum verteilen von Vote Belohnungen.
 * 
 * @author Calenria
 */
public class GenModWorld extends JavaPlugin {
    /**
     * Standart Bukkit Logger.
     */
    private static Logger log = Logger.getLogger("Minecraft");

    /**
     * Kommandos.
     */
    private CommandsManager<CommandSender> commands;

    /**
     * Vault Permissions.
     */
    private Permission permission = null;
    /**
     * Objekt zum zugriff auf die Konfiguration.
     */
    private ConfigData config = null;

    /**
     * Vault Permissions.
     * 
     * @return the permission
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * @return the config
     */
    public ConfigData getPluginConfig() {
        return config;
    }

    /**
     * Delegiert die registierten Befehle an die jeweiligen Klassen und prüft ob
     * die Benutzung korrekt ist.
     * 
     * @see org.bukkit.plugin.java.JavaPlugin#onCommand(org.bukkit.command.CommandSender,
     *      org.bukkit.command.Command, java.lang.String, java.lang.String[])
     * @param sender
     *            Der Absender des Befehls
     * @param cmd
     *            Das Kommando
     * @param label
     *            Das Label
     * @param args
     *            String Array von Argumenten
     * @return <tt>true</tt> wenn der Befehl erfolgreich ausgeführt worden ist
     */
    @Override
    public final boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        try {
            commands.execute(cmd.getName(), args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(ChatColor.RED + "Du hast keinen zugriff auf diesen Befehl!");
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (CommandUsageException e) {
            sender.sendMessage(ChatColor.RED + e.getLocalizedMessage());
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatColor.RED + "Zahl erwartet, erhielt aber eine Zeichenfolge.");
            } else {
                sender.sendMessage(ChatColor.RED + "Ein Fehler ist aufgetreten, genaueres findest du in der Konsole");
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }

    /**
     * Wird beim auschalten des Plugins aufgerufen.
     * 
     * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
     */
    @Override
    public final void onDisable() {
        log.log(Level.INFO, String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    /**
     * Initialisierung des Plugins.
     * 
     * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
     */
    @Override
    public final void onEnable() {
        setupPermissions();
        setupCommands();
        setupConfig();
        log.log(Level.INFO, String.format("[%s] Enabled Version %s", getDescription().getName(), getDescription().getVersion()));

        this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                log.log(Level.INFO, "Checking Worlds...");
                checkWorlds();
            }
        }, Utils.TASK_ONE_SECOND, Utils.TASK_ONE_MINUTE);

    }

    protected void checkWorlds() {
        File exportDir = new File(this.getPluginConfig().getExportDir());
        String[] files = exportDir.list();
        int fileNum = files.length;
        log.log(Level.INFO, "Found " + fileNum + " Worlds");
        if (fileNum < 10) {
            log.log(Level.INFO, "Generating " + (10 - fileNum) + " Worlds");
            this.genWorlds((10 - fileNum));
        }
    }

    public void genWorlds(Integer count) {
        this.setupConfig();

        Integer from = 0;
        Integer to = 0;
        String worldPraefix = this.getPluginConfig().getWorldPraefix();
        String worldName = "";

        from = this.getPluginConfig().getLastWorld() + 1;
        to = from + (count - 1);

        if (from != null && to != null && from > 0 && to > 0) {
            log.log(Level.INFO, "Creating worlds from: " + from + " to: " + to);
            while (from <= to) {
                this.getPluginConfig().setLastWorld(from);
                this.saveConfig();
                worldName = worldPraefix + from;
                from++;
                doWorldGen(worldName);
            }
        }
    }

    public void doWorldGen(final String worldName) {

        WorldCreator wc = new WorldCreator(worldName);
        wc.type(WorldType.getByName("BIOMESOP"));
        wc.environment(Environment.NORMAL);
        wc.seed(new Random().nextLong());
        wc.generator("BIOMESOP");

        log.log(Level.INFO, "Creating world: " + worldName);

        World newWorld = wc.createWorld();
        newWorld.setDifficulty(Difficulty.NORMAL);
        newWorld.setAutoSave(false);
        newWorld.setKeepSpawnInMemory(true);
        newWorld.setTime(0L);
        newWorld.setSpawnLocation(0, (newWorld.getHighestBlockYAt(0, 0) + 2), 0);
        log.log(Level.INFO, "Saving world: " + worldName);
        newWorld.save();

        log.log(Level.INFO, "Generating Chunks... ");

        HashSet<Chunk> chunks = new HashSet<Chunk>();
        Chunk cchunk = newWorld.getChunkAt(newWorld.getSpawnLocation());
        for (int i = -192; i <= 192; i = i + 16) {
            for (int a = -192; a <= 192; a = a + 16) {
                cchunk = newWorld.getChunkAt((newWorld.getSpawnLocation().getBlockX() + i) >> 4, (newWorld.getSpawnLocation().getBlockZ() + a) >> 4);
                chunks.add(cchunk);
            }
        }

        for (Chunk chunk : chunks) {
            log.log(Level.INFO, "Chunk: " + chunk.getX() + " " + chunk.getZ());
            chunk.load(true);
        }

        log.log(Level.INFO, "Generated Chunks: " + chunks.size());

        log.log(Level.INFO, "Seting World Spawn: 0 " + (newWorld.getHighestBlockYAt(0, 0) + 2) + " 0");
        newWorld.setSpawnLocation(0, (newWorld.getHighestBlockYAt(0, 0) + 2), 0);

        log.log(Level.INFO, "Unloading world: " + worldName);
        newWorld.setKeepSpawnInMemory(false);
        newWorld.save();
        Bukkit.unloadWorld(newWorld, true);

        log.log(Level.INFO, "Moving world: " + worldName + " to " + this.getPluginConfig().getExportDir());

        File worldDir = newWorld.getWorldFolder();
        File exportWorldDir = new File(this.getPluginConfig().getExportDir() + "/" + worldName);
        if (worldDir.isDirectory()) {

            try {
                FileUtils.moveDirectory(worldDir, exportWorldDir);
                Runtime run = Runtime.getRuntime();
                Process pr = run.exec("chmod -R 777 " + exportWorldDir);
                pr.waitFor();
                new File(exportWorldDir + "/session.lock").delete();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    };

    /**
     * Initialisierung der Plugin Befehle.
     */
    private void setupCommands() {
        this.commands = new CommandsManager<CommandSender>() {
            @Override
            public boolean hasPermission(final CommandSender sender, final String perm) {
                return permission.has(sender, perm);
            }
        };

        commands.setInjector(new SimpleInjector(this));
        CommandsManagerRegistration cmdRegister = new CommandsManagerRegistration(this, this.commands);
        cmdRegister.register(GenModWorldCommands.class);
    }

    /**
     * Liest die Konfiguration aus und erzeugt ein ConfigData Objekt.
     */
    public final void setupConfig() {
        if (!new File(this.getDataFolder(), "config.yml").exists()) {
            this.getConfig().options().copyDefaults(true);
            this.saveConfig();
        } else {
            this.reloadConfig();
        }
        this.config = new ConfigData(this);
    }

    /**
     * Liest die Konfiguration aus und erzeugt ein ConfigData Objekt.
     */
    public final void saveConfig() {
        this.config.saveConfig(this, new File(this.getDataFolder(), "config.yml"));

    }

    /**
     * Überprüft ob Vault vorhanden und ein passender Permissionhandler
     * verfügbar ist.
     * 
     * @return <tt>true</tt> wenn ein Vault Permissionhandler gefunden wird.
     */
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
}
