package org.dragonet.bukkit.psbg;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;

/**
 * Created on 2017/11/29.
 */
public class StaticWorldListener implements Listener {

    private final PlayerStevesBattleGrounds plugin;

    public StaticWorldListener(PlayerStevesBattleGrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onWorldLoad(WorldLoadEvent e){
        e.getWorld().setAutoSave(false);
        plugin.getLogger().info(String.format("Auto-save for world [" + e.getWorld().getName() + "] is prevented! "));
    }

    @EventHandler
    private void onChunkUnload(ChunkUnloadEvent e) {
        e.setSaveChunk(false);
    }

}
