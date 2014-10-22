package com.github.calenria.genmodworld.commands;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;

import com.github.calenria.genmodworld.GenModWorld;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;

public class GenModWorldCommands {
    /**
     * Bukkit Logger.
     */
    private static Logger log = Logger.getLogger("Minecraft");
    /**
     * GenModWorld Plugin.
     */
    private GenModWorld plugin;

    /**
     * @param brPlugin
     *            GenModWorld Plugin
     * @return
     */
    public GenModWorldCommands(GenModWorld brPlugin) {
        this.plugin = brPlugin;
    }

    /**
     * Lädt das Plugin neu.
     * 
     * @param args
     *            Sollte leer sein
     * @param sender
     *            Absender des Befehls
     * @throws com.sk89q.minecraft.util.commands.CommandException
     *             CommandException
     */
    @Command(aliases = { "gwreload" }, desc = "Läd das Plugin neu", usage = "reload")
    @CommandPermissions("genworld.reload")
    public final void reload(final CommandContext args, final CommandSender sender) throws CommandException {
        plugin.setupConfig();
        sender.sendMessage(String.format("[%s] reloaded Version %s", plugin.getDescription().getName(), plugin.getDescription().getVersion()));
        log.log(Level.INFO, String.format("[%s] reloaded Version %s", plugin.getDescription().getName(), plugin.getDescription().getVersion()));
    }

    @Command(aliases = { "genworld" }, desc = "Erzeugt eine Welt", usage = "<welt>", min = 1, max = 1)
    @CommandPermissions("genworld.gen")
    public final void genworld(final CommandContext args, final CommandSender sender) throws CommandException {
        String sworld = args.getString(0);
        String worldName = sworld;
        plugin.doWorldGen(worldName);
    }

    @Command(aliases = { "genworlds" }, desc = "Erzeugt eine bestimmte anzahl von Welten", usage = "[<anzahl>]", min = 0, max = 1)
    @CommandPermissions("genworld.gen")
    public final void genworlds(final CommandContext args, final CommandSender sender) throws CommandException {

        Integer count = 0;
        if (args.argsLength() > 0) {
            count = args.getInteger(0);
        } else {
            count = 10;
        }

        plugin.genWorlds(count);
    }

    @Command(aliases = { "tpw" }, desc = "Portet in eine Welt", usage = "<welt>", min = 1, max = 1)
    @CommandPermissions("genworld.tpw")
    public final void tpw(final CommandContext args, final CommandSender sender) throws CommandException {
        String sworld = args.getString(0);
        String worldFolder = Bukkit.getWorld("world").getWorldFolder().getPath();

        File world = new File(worldFolder + "/" + sworld);
        log.log(Level.INFO, "Teleporting to World: " + world.toString());
        log.log(Level.INFO, "Teleporting to World: " + world.getName());

        if (world.isDirectory()) {
            Bukkit.getPlayer(sender.getName()).teleport(Bukkit.createWorld(new WorldCreator(sworld)).getSpawnLocation());
        } else {
            sender.sendMessage("Welt existiert nicht!");
        }

    };

    public static Collection<File> listFileTree(File dir) {
        Set<File> fileTree = new HashSet<File>();
        for (File entry : dir.listFiles()) {
            if (entry.isFile())
                fileTree.add(entry);
            else
                fileTree.addAll(listFileTree(entry));
        }
        return fileTree;
    }
}
