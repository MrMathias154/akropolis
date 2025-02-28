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

package team.devblook.akropolis.module.modules.world;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import team.devblook.akropolis.AkropolisPlugin;
import team.devblook.akropolis.Permissions;
import team.devblook.akropolis.config.ConfigType;
import team.devblook.akropolis.config.Messages;
import team.devblook.akropolis.cooldown.CooldownType;
import team.devblook.akropolis.module.Module;
import team.devblook.akropolis.module.ModuleType;
import team.devblook.akropolis.module.modules.hologram.Hologram;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("deprecation")
public class WorldProtect extends Module {
    private boolean hungerLoss;
    private boolean fallDamage;
    private boolean weatherChange;
    private boolean deathMessage;
    private boolean fireSpread;
    private boolean leafDecay;
    private boolean mobSpawning;
    private boolean blockBurn;
    private boolean voidDeath;
    private boolean itemDrop;
    private boolean itemPickup;
    private boolean blockBreak;
    private boolean blockPlace;
    private boolean blockInteract;
    private boolean playerPvP;
    private boolean playerDrowning;
    private boolean fireDamage;

    private final List<Material> interactables = Arrays.asList(XMaterial.ACACIA_DOOR.parseMaterial(),
            XMaterial.ACACIA_FENCE_GATE.parseMaterial(), XMaterial.ANVIL.parseMaterial(),
            XMaterial.FLOWER_POT.parseMaterial(), XMaterial.PAINTING.parseMaterial(), XMaterial.BEACON.parseMaterial(),
            XMaterial.RED_BED.parseMaterial(), XMaterial.BIRCH_DOOR.parseMaterial(),
            XMaterial.BIRCH_FENCE_GATE.parseMaterial(), XMaterial.OAK_BOAT.parseMaterial(),
            XMaterial.BREWING_STAND.parseMaterial(), XMaterial.COMMAND_BLOCK.parseMaterial(),
            XMaterial.CHEST.parseMaterial(), XMaterial.DARK_OAK_DOOR.parseMaterial(),
            XMaterial.SPRUCE_DOOR.parseMaterial(), XMaterial.DARK_OAK_FENCE_GATE.parseMaterial(),
            XMaterial.DAYLIGHT_DETECTOR.parseMaterial(), XMaterial.DAYLIGHT_DETECTOR.parseMaterial(),
            XMaterial.DISPENSER.parseMaterial(), XMaterial.DROPPER.parseMaterial(),
            XMaterial.ENCHANTING_TABLE.parseMaterial(), XMaterial.ENDER_CHEST.parseMaterial(),
            XMaterial.OAK_FENCE_GATE.parseMaterial(), XMaterial.FURNACE.parseMaterial(),
            XMaterial.HOPPER.parseMaterial(), XMaterial.HOPPER_MINECART.parseMaterial(),
            XMaterial.ITEM_FRAME.parseMaterial(), XMaterial.JUNGLE_DOOR.parseMaterial(),
            XMaterial.JUNGLE_FENCE_GATE.parseMaterial(), XMaterial.LEVER.parseMaterial(),
            XMaterial.MINECART.parseMaterial(), XMaterial.NOTE_BLOCK.parseMaterial(),
            XMaterial.MINECART.parseMaterial(), XMaterial.COMPARATOR.parseMaterial(),
            XMaterial.ACACIA_SIGN.parseMaterial(), XMaterial.BIRCH_SIGN.parseMaterial(),
            XMaterial.DARK_OAK_SIGN.parseMaterial(), XMaterial.JUNGLE_SIGN.parseMaterial(),
            XMaterial.OAK_SIGN.parseMaterial(), XMaterial.CHEST_MINECART.parseMaterial(),
            XMaterial.OAK_DOOR.parseMaterial(), XMaterial.OAK_TRAPDOOR.parseMaterial(),
            XMaterial.TRAPPED_CHEST.parseMaterial(), XMaterial.OAK_BUTTON.parseMaterial(),
            XMaterial.OAK_DOOR.parseMaterial());

    public WorldProtect(AkropolisPlugin plugin) {
        super(plugin, ModuleType.WORLD_PROTECT);
    }

    @Override
    public void onEnable() {
        FileConfiguration config = getConfig(ConfigType.SETTINGS);
        hungerLoss = config.getBoolean("world_settings.disable_hunger_loss");
        fallDamage = config.getBoolean("world_settings.disable_fall_damage");
        playerPvP = config.getBoolean("world_settings.disable_player_pvp");
        voidDeath = config.getBoolean("world_settings.disable_void_death");
        weatherChange = config.getBoolean("world_settings.disable_weather_change");
        deathMessage = config.getBoolean("world_settings.disable_death_message");
        mobSpawning = config.getBoolean("world_settings.disable_mob_spawning");
        itemDrop = config.getBoolean("world_settings.disable_item_drop");
        itemPickup = config.getBoolean("world_settings.disable_item_pickup");
        blockBreak = config.getBoolean("world_settings.disable_block_break");
        blockPlace = config.getBoolean("world_settings.disable_block_place");
        blockInteract = config.getBoolean("world_settings.disable_block_interact");
        blockBurn = config.getBoolean("world_settings.disable_block_burn");
        fireSpread = config.getBoolean("world_settings.disable_block_fire_spread");
        leafDecay = config.getBoolean("world_settings.disable_block_leaf_decay");
        playerDrowning = config.getBoolean("world_settings.disable_drowning");
        fireDamage = config.getBoolean("world_settings.disable_fire_damage");
    }

    @Override
    public void onDisable() {
        // TODO: Refactor to follow Liskov Substitution principle.
    }

    @EventHandler
    public void onArmorStandInteract(PlayerArmorStandManipulateEvent event) {
        for (Hologram entry : getPlugin().getHologramManager().getHolograms()) {
            for (ArmorStand stand : entry.getStands()) {
                if (stand.equals(event.getRightClicked())) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!blockBreak || event.isCancelled())
            return;

        Player player = event.getPlayer();
        if (inDisabledWorld(player.getLocation()))
            return;
        if (player.hasPermission(Permissions.EVENT_BLOCK_BREAK.getPermission()))
            return;

        event.setCancelled(true);

        if (tryCooldown(player.getUniqueId(), CooldownType.BLOCK_BREAK, 3)) {
            player.sendMessage(Messages.EVENT_BLOCK_BREAK.toComponent());
        }
    }

    @EventHandler
    public void onBlockPlace(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (!blockPlace || event.isCancelled())
            return;

        Player player = event.getPlayer();

        if (inDisabledWorld(player.getLocation()))
            return;

        ItemStack item = event.getItem();

        if (item == null) return;

        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();

        if (container.has(NamespacedKey.minecraft("hotbar-item"), PersistentDataType.STRING)) {
            event.setCancelled(true);
            return;
        }

        if (player.hasPermission(Permissions.EVENT_BLOCK_PLACE.getPermission()))
            return;

        event.setCancelled(true);

        if (tryCooldown(event.getPlayer().getUniqueId(), CooldownType.BLOCK_PLACE, 3)) {
            player.sendMessage(Messages.EVENT_BLOCK_PLACE.toComponent());
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (!blockBurn)
            return;

        if (inDisabledWorld(event.getBlock().getLocation()))
            return;

        event.setCancelled(true);
    }

    // Prevent destroying of item frame/paintings
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDestroy(HangingBreakByEntityEvent event) {
        if (!blockBreak || inDisabledWorld(event.getEntity().getLocation()))
            return;

        Entity entity = event.getEntity();
        Entity player = event.getRemover();

        if (player == null) return;

        if (entity instanceof Painting || entity instanceof ItemFrame && player instanceof Player) {
            if (player.hasPermission(Permissions.EVENT_BLOCK_BREAK.getPermission()))
                return;

            event.setCancelled(true);

            if (tryCooldown(player.getUniqueId(), CooldownType.BLOCK_BREAK, 3)) {
                player.sendMessage(Messages.EVENT_BLOCK_BREAK.toComponent());
            }
        }
    }

    // Prevent items being rotated in item frame
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (!blockInteract || inDisabledWorld(event.getRightClicked().getLocation()))
            return;

        Entity entity = event.getRightClicked();
        Entity player = event.getPlayer();

        if (player.hasPermission(Permissions.EVENT_BLOCK_INTERACT.getPermission()))
            return;

        if (entity instanceof ItemFrame) {
            event.setCancelled(true);

            if (tryCooldown(player.getUniqueId(), CooldownType.BLOCK_INTERACT, 3)) {
                player.sendMessage(Messages.EVENT_BLOCK_INTERACT.toComponent());
            }
        }
    }

    // Prevent items being taken from item frames
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!blockInteract || inDisabledWorld(event.getEntity().getLocation()))
            return;

        Entity entity = event.getEntity();
        Entity damager = event.getDamager();

        if (entity instanceof ItemFrame && damager instanceof Player player) {

            if (player.hasPermission(Permissions.EVENT_BLOCK_INTERACT.getPermission()))
                return;

            event.setCancelled(true);

            if (tryCooldown(player.getUniqueId(), CooldownType.BLOCK_INTERACT, 3)) {
                player.sendMessage(Messages.EVENT_BLOCK_INTERACT.toComponent());
            }
        }
    }

    // TODO: Reduce cognitive complexity from 17 to something minor.
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockInteract(PlayerInteractEvent event) {
        if (!blockInteract || inDisabledWorld(event.getPlayer().getLocation()))
            return;

        Player player = event.getPlayer();

        if (player.hasPermission(Permissions.EVENT_BLOCK_INTERACT.getPermission()))
            return;

        Block block = event.getClickedBlock();

        if (block == null)
            return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            for (Material material : interactables) {
                if (block.getType() == material || block.getType().toString().contains("POTTED")) {
                    event.setCancelled(true);

                    if (tryCooldown(player.getUniqueId(), CooldownType.BLOCK_INTERACT, 3)) {
                        player.sendMessage(Messages.EVENT_BLOCK_INTERACT.toComponent());
                    }

                    return;
                }
            }
        } else if (event.getAction() == Action.PHYSICAL && block.getType() == XMaterial.FARMLAND.parseMaterial()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;

        if (inDisabledWorld(player.getLocation()))
            return;

        if (fallDamage && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        } else if (playerDrowning && event.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
            event.setCancelled(true);
        } else if (fireDamage && (event.getCause() == EntityDamageEvent.DamageCause.FIRE
                || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK
                || event.getCause() == EntityDamageEvent.DamageCause.LAVA)) {
            event.setCancelled(true);
        } else if (voidDeath && event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            player.setFallDistance(0.0F);

            Location location = ((LobbySpawn) getPlugin().getModuleManager().getModule(ModuleType.LOBBY)).getLocation();

            if (location == null)
                return;

            Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> player.teleportAsync(location), 3L);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFireSpread(BlockIgniteEvent event) {
        if (!fireSpread)
            return;

        if (inDisabledWorld(event.getBlock().getLocation()))
            return;

        if (event.getCause() == BlockIgniteEvent.IgniteCause.SPREAD)
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (!hungerLoss)
            return;

        if (!(event.getEntity() instanceof Player player))
            return;

        if (inDisabledWorld(player.getLocation()))
            return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDropEvent(PlayerDropItemEvent event) {
        if (!itemDrop)
            return;

        Player player = event.getPlayer();

        if (inDisabledWorld(player.getLocation()))
            return;

        if (player.hasPermission(Permissions.EVENT_ITEM_DROP.getPermission()))
            return;

        event.setCancelled(true);

        if (tryCooldown(player.getUniqueId(), CooldownType.ITEM_DROP, 3)) {
            player.sendMessage(Messages.EVENT_ITEM_DROP.toComponent());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerPickupEvent(PlayerPickupItemEvent event) {
        if (!itemPickup)
            return;

        Player player = event.getPlayer();

        if (inDisabledWorld(player.getLocation()))
            return;

        if (player.hasPermission(Permissions.EVENT_ITEM_PICKUP.getPermission()))
            return;

        event.setCancelled(true);

        if (tryCooldown(player.getUniqueId(), CooldownType.ITEM_PICKUP, 3)) {
            player.sendMessage(Messages.EVENT_ITEM_PICKUP.toComponent());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeafDecay(LeavesDecayEvent event) {
        if (!leafDecay)
            return;

        if (inDisabledWorld(event.getBlock().getLocation()))
            return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!mobSpawning)
            return;

        if (inDisabledWorld(event.getEntity().getLocation()))
            return;

        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM)
            return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWeatherChange(WeatherChangeEvent event) {
        if (inDisabledWorld(event.getWorld()))
            return;

        if (!weatherChange)
            return;

        event.setCancelled(event.toWeatherState());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!deathMessage)
            return;

        if (inDisabledWorld(event.getEntity().getLocation()))
            return;

        event.setDeathMessage(null);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!playerPvP)
            return;

        if (!(event.getEntity() instanceof Player player))
            return;

        if (inDisabledWorld(player.getLocation()))
            return;

        if (event.getDamager().hasPermission(Permissions.EVENT_PLAYER_PVP.getPermission()))
            return;

        event.setCancelled(true);

        if (tryCooldown(player.getUniqueId(), CooldownType.PLAYER_PVP, 3)) {
            event.getDamager().sendMessage(Messages.EVENT_PLAYER_PVP.toComponent());
        }
    }
}
