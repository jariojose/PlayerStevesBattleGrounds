package org.dragonet.bukkit.psbg;

import org.dragonet.bukkit.psbg.tasks.BattlesTimerTask;
import org.dragonet.bukkit.psbg.tasks.ShrinkBorderTask;

/**
 * Created on 2017/12/2.
 */
public class Battles {

    private final PlayerStevesBattleGrounds plugin;

    private boolean started;
    private ShrinkBorderTask taskShrinkBorder;
    private BattlesTimerTask taskTimer;

    public Battles(PlayerStevesBattleGrounds plugin) {
        this.plugin = plugin;
    }

    public void startBattles() {
        if(started) {
            throw new IllegalStateException();
        }
        started = true;
    }

    public void endBattles() {
        if(!started) {
            throw new IllegalStateException();
        }
        started = false;
        if(taskShrinkBorder != null) {
            taskShrinkBorder.cancel();
            taskShrinkBorder = null;
        }
        if(taskTimer != null) {
            taskTimer.cancel();
            taskTimer = null;
        }

        // TODO: teleport all players back to the hub
        // ...

        plugin.getServer().shutdown();
    }
}
