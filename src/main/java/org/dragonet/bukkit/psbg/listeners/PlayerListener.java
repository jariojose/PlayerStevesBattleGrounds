package org.dragonet.bukkit.psbg.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.dragonet.bukkit.psbg.GamePhase;
import org.dragonet.bukkit.psbg.PlayerStevesBattleGrounds;
import org.dragonet.bukkit.psbg.PlayerTag;
import org.dragonet.bukkit.psbg.PlayerTagMetadata;
import org.dragonet.bukkit.psbg.utils.InventoryUtils;
import org.dragonet.bukkit.psbg.utils.Lang;

/**
 * Created on 2017/11/30.
 */
public class PlayerListener implements Listener {

    private final PlayerStevesBattleGrounds plugin;

    private Location locationWaitSpawn;

    public PlayerListener(PlayerStevesBattleGrounds plugin) {
        this.plugin = plugin;
    }

    /**
     * this handler make player be only these two kinds:
     * 1. join and wait the game to start
     * 2. game is running so become a spectator
     * @param e
     */
    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        if(plugin.getPhase().equals(GamePhase.WAIT)) {
            // wait or kick
            if (plugin.getServer().getOnlinePlayers().size() >= plugin.getConfig().getInt("max-players")) {
                e.setJoinMessage(null);
                e.getPlayer().kickPlayer(Lang.build("messages.server-full"));
            } else {
                if(locationWaitSpawn == null) {
                    locationWaitSpawn = new Location(
                            plugin.getServer().getWorld(plugin.getConfig().getString("wait-spawn.world")),
                            plugin.getConfig().getDouble("wait-spawn.x"),
                            plugin.getConfig().getDouble("wait-spawn.y"),
                            plugin.getConfig().getDouble("wait-spawn.z")
                    );
                }
                e.setJoinMessage(Lang.build("messages.join.wait",
                        e.getPlayer().getName(),
                        plugin.getServer().getOnlinePlayers().size(),
                        plugin.getConfig().getInt("max-players"),
                        plugin.getConfig().getInt("min-players")
                        ));
                InventoryUtils.clearInventory(e.getPlayer().getInventory());
                e.getPlayer().setMetadata(PlayerStevesBattleGrounds.PLAYER_TAG_METADATA_KEY, new FixedMetadataValue(plugin, new PlayerTagMetadata(PlayerTag.NORMAL)));
                e.getPlayer().teleport(locationWaitSpawn);
            }
        } else {
            // spectate
            e.getPlayer().setMetadata(PlayerStevesBattleGrounds.PLAYER_TAG_METADATA_KEY, new FixedMetadataValue(plugin, new PlayerTagMetadata(PlayerTag.SPECTATING)));
            // TODO: teleport to a random in-game player
        }
    }
}
