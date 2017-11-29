package org.dragonet.bukkit.psbg;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created on 2017/11/29.
 */
public class PlayerStevesBattleGrounds extends JavaPlugin {

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(new StaticWorldListener(this), this);
    }
}
