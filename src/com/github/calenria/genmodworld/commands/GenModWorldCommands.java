package com.github.calenria.genmodworld.commands;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import biomesoplenty.world.WorldTypeBOP;

import com.github.calenria.genmodworld.GenModWorld;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;

public class GenModWorldCommands {
    /**
     * GenModWorld Plugin.
     */
    final private GenModWorld plugin;

    /**
     * @param brPlugin
     *            GenModWorld Plugin
     * @return
     */
    public GenModWorldCommands(final GenModWorld brPlugin) {
        this.plugin = brPlugin;
    }

    @Command(aliases = { "breplace" }, desc = "Ersetzt  blöcke in allen geladenen Chunks", usage = "<welt> [itemid:data] [itemid:data]", min = 1, max = 3)
    @CommandPermissions("blockreplace.replace")
    public final void breplace(final CommandContext args, final CommandSender sender) throws CommandException {
        String sworld = args.getString(0);
        WorldCreator wc = new WorldCreator("GMW-" + sworld);
        wc.type(WorldType.getByName("BIOMESOP"));
        wc.environment(Environment.NORMAL);
        wc.seed(new Random().nextLong());
        wc.generator("BIOMESOP");
        World newWorld = wc.createWorld();
        newWorld.setDifficulty(Difficulty.NORMAL);
        newWorld.setAutoSave(true);
        newWorld.setKeepSpawnInMemory(false);
        newWorld.setTime(0L);
    }

    private int replaceBlocks(Chunk chunk, Integer matid, byte dataid, int rematid, byte redataid) {
        int cnt = 0;
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    Block block = chunk.getBlock(x, y, z);
                    if (block.getTypeId() == matid && block.getData() == dataid) {
                        block.setTypeIdAndData(rematid, redataid, true);
                        cnt++;
                    }
                }
            }
        }

        return cnt;
    }
}
