package org.dragonet.bukkit.psbg;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.dragonet.bukkit.psbg.listeners.PlayerListener;
import org.dragonet.bukkit.psbg.listeners.StaticWorldListener;
import org.dragonet.bukkit.psbg.tasks.CountDownTask;
import org.dragonet.bukkit.psbg.utils.Lang;

/**
 * Created on 2017/11/29.
 */
public class PlayerStevesBattleGrounds extends JavaPlugin {

    public final static String PLAYER_TAG_METADATA_KEY = "PSBG_PlayerTag";

    private YamlConfiguration config;

    private GamePhase phase = GamePhase.WAIT;
    private CountDownTask countDownTask = new CountDownTask(this);

    @Override
    public void onEnable() {
        saveResource("lang.yml", false);
        saveResource("config.yml", false);

        Lang.lang = YamlConfiguration.loadConfiguration(new java.io.File(getDataFolder(), "lang.yml"));
        config = YamlConfiguration.loadConfiguration(new java.io.File(getDataFolder(), "config.yml"));

        countDownTask.init();

        getServer().getPluginManager().registerEvents(new StaticWorldListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    /**
     * called when timer counts down to zero.
     */
    public void onTimerEnded() {

    }

    public GamePhase getPhase() {
        return phase;
    }

    @Override
    public YamlConfiguration getConfig() {
        return config;
    }
}
