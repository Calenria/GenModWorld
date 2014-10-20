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
package com.github.calenria.genmodworld.models;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

import com.github.calenria.genmodworld.GenModWorld;

/**
 * Konfigurations Klasse.
 * 
 * @author Calenria
 * 
 */
public class ConfigData {

    /**
     * Die gewählte Sprache.
     */
    private String lang;
    /**
     * Welten Präfix.
     */
    private String worldPraefix;
    /**
     * Zuletzt generierte Welt.
     */
    private Integer lastWorld;
    
    private String exportDir;
    
    /**
     * @param plugin
     *            NextVote Plugin
     */
    public ConfigData(GenModWorld plugin) {
        FileConfiguration config = plugin.getConfig();
        setLang(config.getString("lang"));
        setLastWorld(config.getInt("lastWorldNumber"));
        setWorldPraefix(config.getString("worldPraefix"));
        setExportDir(config.getString("exportDir"));
        
    }
    
    /**
     * @return the lang
     */
    public final String getExportDir() {
        return exportDir;
    }

    /**
     * @param sExportDir
     *            Export Dir
     */
    public final void setExportDir(final String sExportDir) {
        this.exportDir = sExportDir;
    }
    
    /**
     * @return the lang
     */
    public final String getLang() {
        return lang;
    }

    /**
     * @param cLang
     *            the lang to set
     */
    public final void setLang(final String cLang) {
        this.lang = cLang;
    }
    
    /**
     * @return the last World number
     */
    public final Integer getLastWorld() {
        return lastWorld;
    }

    /**
     * @param iLastWorld
     *            the lastWorld to set
     */
    public final void setWorldPraefix(final String sWorldPraefix) {
        this.worldPraefix = sWorldPraefix;
    }

    
    /**
     * @return the last World number
     */
    public final String getWorldPraefix() {
        return worldPraefix;
    }

    /**
     * @param iLastWorld
     *            the lastWorld to set
     */
    public final void setLastWorld(final Integer iLastWorld) {
        this.lastWorld = iLastWorld;
    }

    public void saveConfig(GenModWorld plugin, File file) {
        FileConfiguration config = plugin.getConfig();
        config.set("lang", "de");
        config.set("lastWorldNumber", getLastWorld());
        config.set("worldPraefix", getWorldPraefix());
        config.set("exportDir", getExportDir());
        
        try {
            config.save(file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}