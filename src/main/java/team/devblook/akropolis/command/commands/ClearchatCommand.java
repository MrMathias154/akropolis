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

package team.devblook.akropolis.command.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.devblook.akropolis.AkropolisPlugin;
import team.devblook.akropolis.Permissions;
import team.devblook.akropolis.command.InjectableCommand;
import team.devblook.akropolis.config.Messages;
import team.devblook.akropolis.util.TextUtil;

import java.util.List;

public class ClearchatCommand extends InjectableCommand {

    public ClearchatCommand(AkropolisPlugin plugin, List<String> aliases) {
        super(plugin, "clearchat", "Clear global or a player's chat", "/clearchat [player]", aliases);
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender.hasPermission(Permissions.COMMAND_CLEARCHAT.getPermission()))) {
            sender.sendMessage(Messages.NO_PERMISSION.toComponent());
            return;
        }

        if (args.length == 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                for (int i = 0; i < 100; i++) {
                    player.sendMessage("");
                }

                player.sendMessage(TextUtil.replace(Messages.CLEARCHAT.toComponent(), "player", sender.name()));
            }
        } else if (args.length == 1) {

            Player player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                sender.sendMessage(TextUtil.replace(Messages.INVALID_PLAYER.toComponent(), "player", TextUtil.parse(args[0])));
                return;
            }

            for (int i = 0; i < 100; i++) {
                player.sendMessage("");
            }

            sender.sendMessage(TextUtil.replace(Messages.CLEARCHAT_PLAYER.toComponent(), "player", sender.name()));
        }

    }
}
