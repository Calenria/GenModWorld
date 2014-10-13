package com.github.calenria.blockreplacer.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import com.github.calenria.blockreplacer.BlockReplace;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;

public class BlockReplaceCommands {
    /**
     * BlockReplace Plugin.
     */
    final private BlockReplace plugin;

    /**
     * @param brPlugin
     *            BlockReplace Plugin
     * @return
     */
    public BlockReplaceCommands(final BlockReplace brPlugin) {
        this.plugin = brPlugin;
    }

    @Command(aliases = { "breplace" }, desc = "Ersetzt  blöcke in allen geladenen Chunks", usage = "<welt> [itemid:data] [itemid:data]", min = 1, max = 3)
    @CommandPermissions("blockreplace.replace")
    public final void breplace(final CommandContext args, final CommandSender sender) throws CommandException {
        String sworld = args.getString(0);
        World world = Bukkit.getWorld(sworld);
        Chunk[] chunks = world.getLoadedChunks();
        int cnt = 0;
        if (args.argsLength() == 1) {
            List<String> replaces = plugin.config.getReplace();
            for (Chunk chunk : chunks) {
                for (String repl : replaces) {
                    String[] tmp = repl.split(",");
                    String[] mats = tmp[0].split(":");
                    String[] remats = tmp[1].split(":");
                    Integer matid = Integer.parseInt(mats[0]);
                    byte dataid = ((Integer) Integer.parseInt(mats[1])).byteValue();
                    int rematid = Integer.parseInt(remats[0]);
                    byte redataid = ((Integer) Integer.parseInt(remats[1])).byteValue();
                    cnt += replaceBlocks(chunk, matid, dataid, rematid, redataid);
                }
            }
        } else {
            for (Chunk chunk : chunks) {
                String[] mats = args.getString(1).split(":");
                String[] remats = args.getString(2).split(":");
                Integer matid = Integer.parseInt(mats[0]);
                byte dataid = ((Integer) Integer.parseInt(mats[1])).byteValue();
                int rematid = Integer.parseInt(remats[0]);
                byte redataid = ((Integer) Integer.parseInt(remats[1])).byteValue();
                cnt += replaceBlocks(chunk, matid, dataid, rematid, redataid);
            }
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + cnt + " &4 Blöcke ersetzt"));
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
