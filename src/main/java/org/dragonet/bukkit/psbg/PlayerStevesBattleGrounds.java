package org.dragonet.bukkit.psbg;

import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.dragonet.bukkit.psbg.listeners.PlayerListener;
import org.dragonet.bukkit.psbg.listeners.StaticWorldListener;
import org.dragonet.bukkit.psbg.tasks.CountDownProcessor;
import org.dragonet.bukkit.psbg.utils.InventoryUtils;
import org.dragonet.bukkit.psbg.utils.Lang;

/**
 * Created on 2017/11/29.
 */
public class PlayerStevesBattleGrounds extends JavaPlugin {

    public final static String PLAYER_TAG_METADATA_KEY = "PSBG_PlayerTag";

    private YamlConfiguration config;

    private GamePhase phase = GamePhase.WAIT;
    private CountDownProcessor waitTimer = new CountDownProcessor(this);

    private Battles battles;

    @Override
    public void onEnable() {
        saveResource("lang.yml", false);
        saveResource("config.yml", false);

        Lang.lang = YamlConfiguration.loadConfiguration(new java.io.File(getDataFolder(), "lang.yml"));
        config = YamlConfiguration.loadConfiguration(new java.io.File(getDataFolder(), "config.yml"));

        waitTimer.init();

        getServer().getPluginManager().registerEvents(new StaticWorldListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(waitTimer, this);

        waitTimer.startTiming();
    }

    /**
     * called when timer counts down to zero.
     */
    public void onTimerEnded() {
        int currentPlayers = getServer().getOnlinePlayers().size();
        if(currentPlayers < config.getInt("min-players")) {
            getServer().broadcastMessage(Lang.build("messages.start.not-enough-players", config.getInt("min-players")));
            waitTimer.startTiming();
            return;
        }

        // starts the match, all player should have NORMAL tag

        getServer().getOnlinePlayers().forEach(c -> {
            InventoryUtils.clearInventory(c.getInventory());
            c.playSound(c.getLocation(), "psbg.battle-begin", 1f, 1f);
            c.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
                    20*10,
                    1,
                    true,
                    false), true);
            Lang.sendMessage(c, "messages.start.begin");
        });

        World w = getServer().getWorld(config.getString("battle-world.world"));
        WorldBorder border = w.getWorldBorder();
        border.setCenter(
                config.getInt("battle-world.center-x"),
                config.getInt("battle-world.center-z")
        );

        battles = new Battles(this);
        battles.startBattles();
    }

    public GamePhase getPhase() {
        return phase;
    }

    @Override
    public YamlConfiguration getConfig() {
        return config;
    }
}
