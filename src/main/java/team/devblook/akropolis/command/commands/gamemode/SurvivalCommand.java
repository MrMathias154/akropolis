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

package team.devblook.akropolis.command.commands.gamemode;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.devblook.akropolis.AkropolisPlugin;
import team.devblook.akropolis.Permissions;
import team.devblook.akropolis.command.InjectableCommand;
import team.devblook.akropolis.config.Messages;
import team.devblook.akropolis.util.TextUtil;

import java.util.List;

public class SurvivalCommand extends InjectableCommand {

    public SurvivalCommand(AkropolisPlugin plugin, List<String> aliases) {
        super(plugin, "gms", "Change to survival mode", "/gms [player]", aliases);
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Messages.CONSOLE_NOT_ALLOWED.toComponent());
                return;
            }

            if (!player.hasPermission(Permissions.COMMAND_GAMEMODE.getPermission())) {
                sender.sendMessage(Messages.NO_PERMISSION.toComponent());
                return;
            }

            player.sendMessage(TextUtil.replace(Messages.GAMEMODE_CHANGE.toComponent(), "gamemode", TextUtil.parse("SURVIVAL")));
            player.setGameMode(GameMode.SURVIVAL);
        } else if (args.length == 1) {
            if (!sender.hasPermission(Permissions.COMMAND_GAMEMODE_OTHERS.getPermission())) {
                sender.sendMessage(Messages.NO_PERMISSION.toComponent());
                return;
            }

            Player player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                sender.sendMessage(TextUtil.replace(Messages.INVALID_PLAYER.toComponent(), "player", Component.text(args[0])));
                return;
            }

            player.sendMessage(TextUtil.replace(Messages.GAMEMODE_CHANGE.toComponent(), "gamemode", Component.text("SURVIVAL")));
            sender.sendMessage(TextUtil.replace(TextUtil.replace(Messages.GAMEMODE_CHANGE_OTHER.toComponent(), "player", player.name()), "gamemode", Component.text("SURVIVAL")));
            player.setGameMode(GameMode.SURVIVAL);
        }

    }
}
