/*
 * This file is part of Akropolis
 *
 * Copyright (c) 2022 DevBlook Team and others
 *
 * Akropolis free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Akropolis is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Akropolis. If not, see <http://www.gnu.org/licenses/>.
 */

package team.devblook.akropolis.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlaceholderUtil {
    private static boolean papi = false;

    private PlaceholderUtil() {
        throw new UnsupportedOperationException();
    }

    public static Component setPlaceholders(String rawText, Player player) {
        Component text = TextUtil.parse(rawText);

        if (rawText.contains("<player>") && player != null) {
            text = TextUtil.parseAndReplace(TextUtil.raw(text), "player", player.name());
        }

        if (rawText.contains("<online>")) {
            text = TextUtil.parseAndReplace(TextUtil.raw(text), "online", Component.text(Bukkit.getOnlinePlayers().size()));
        }

        if (rawText.contains("<online_max>")) {
            text = TextUtil.parseAndReplace(TextUtil.raw(text), "online_max", Component.text(Bukkit.getMaxPlayers()));
        }

        if (rawText.contains("<location>") && player != null) {
            Location l = player.getLocation();
            text = TextUtil.parseAndReplace(TextUtil.raw(text), "location", Component.text(l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ()));
        }

        if (rawText.contains("<ping>") && player != null) {
            text = TextUtil.parseAndReplace(TextUtil.raw(text), "ping", Component.text(player.getPing()));
        }

        if (rawText.contains("<world>") && player != null) {
            text = TextUtil.parseAndReplace(TextUtil.raw(text), "world", Component.text(player.getWorld().getName()));
        }

        if (papi && PlaceholderAPI.containsPlaceholders(TextUtil.raw(text)) && player != null) {
            text = TextUtil.parse(PlaceholderAPI.setPlaceholders(player, TextUtil.raw(text)));
        }

        return text;
    }

    public static void setPapiState(boolean papiState) {
        papi = papiState;
    }
}