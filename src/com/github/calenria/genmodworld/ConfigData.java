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

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Konfigurations Klasse.
 * 
 * @author Calenria
 * 
 */
public class ConfigData {

    private List<String> replace;

    /**
     * @param plugin
     *            BungeeTools Plugin
     */
    public ConfigData(final GenModWorld plugin) {
        FileConfiguration config = plugin.getConfig();
        setReplace(config.getStringList("replace"));
    }

    /**
     * @return the replace
     */
    public List<String> getReplace() {
        return replace;
    }

    /**
     * @param replace
     *            the replace to set
     */
    public void setReplace(List<String> replace) {
        this.replace = replace;
    }

}
