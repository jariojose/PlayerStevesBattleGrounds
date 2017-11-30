package org.dragonet.bukkit.psbg.tasks;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitTask;
import org.dragonet.bukkit.psbg.PlayerStevesBattleGrounds;

/**
 * Created on 2017/11/30.
 */
public class CountDownTask implements Runnable {

    private final PlayerStevesBattleGrounds plugin;

    private BukkitTask task;

    public BossBar bar;
    public int remainingSeconds;

    public CountDownTask(PlayerStevesBattleGrounds plugin) {
        this.plugin = plugin;
    }

    public void init() {
        bar = plugin.getServer().createBossBar("", BarColor.WHITE, BarStyle.SOLID);
    }

    @Override
    public void run() {
        if(remainingSeconds <= 0) {
            plugin.onTimerEnded();
            stopTiming();
            return;
        }
    }

    private void resetTimerByPlayersOnline() {
        int players = plugin.getServer().getOnlinePlayers().size();
        int max = plugin.getConfig().getInt("max-players");
        if(players < max) {
            remainingSeconds = plugin.getConfig().getInt("wait-time.normal");
        } else {
            remainingSeconds = plugin.getConfig().getInt("wait-time.full");
        }
    }

    public void startTiming() {
        if(task != null) throw new IllegalStateException("Timer already started! ");
        resetTimerByPlayersOnline();
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, this, 0L, 20L);
        plugin.getServer().getOnlinePlayers().forEach(bar::addPlayer);
    }

    public void stopTiming() {
        if(task != null) {
            task.cancel();
            task = null;
        }
        if(bar != null) {
            bar.removeAll();
        }
    }
}
