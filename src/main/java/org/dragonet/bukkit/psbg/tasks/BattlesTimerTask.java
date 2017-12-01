package org.dragonet.bukkit.psbg.tasks;

import org.dragonet.bukkit.psbg.Battles;
import org.dragonet.bukkit.psbg.PlayerStevesBattleGrounds;

/**
 * Created on 2017/12/2.
 */
public class BattlesTimerTask extends ManagedTask {

    private final PlayerStevesBattleGrounds plugin;
    private final Battles battles;

    public BattlesTimerTask(PlayerStevesBattleGrounds plugin, Battles battles) {
        this.plugin = plugin;
        this.battles = battles;
    }

    @Override
    public void run() {

    }
}
