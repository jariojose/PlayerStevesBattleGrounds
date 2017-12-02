package org.dragonet.bukkit.psbg;

import es.pollitoyeye.Bikes.ParachuteManager;
import es.pollitoyeye.Bikes.ParachuteType;
import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.dragonet.bukkit.psbg.tasks.BattlesTimerTask;
import org.dragonet.bukkit.psbg.tasks.ShrinkBorderTask;
import org.dragonet.bukkit.psbg.utils.InventoryUtils;
import org.dragonet.bukkit.psbg.utils.Lang;

import java.util.Random;

/**
 * Created on 2017/12/2.
 */
public class Battles {

    private final PlayerStevesBattleGrounds plugin;

    private boolean started;
    private World world;
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

        world = plugin.getServer().getWorld(plugin.getConfig().getString("battles.world"));

        // prepare everyone
        plugin.getServer().getOnlinePlayers().forEach(c -> {
            InventoryUtils.clearInventory(c.getInventory());
            c.playSound(c.getLocation(), "psbg.battle-begin", 1f, 1f);
            c.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
                    20*2,
                    1,
                    true,
                    false), true);
            Lang.sendMessage(c, "messages.start.begin");
        });

        // prepare scoreboard
        Scoreboard sb = plugin.getServer().getScoreboardManager().getNewScoreboard();
        {
            Objective title_objective = sb.registerNewObjective("A", "B");
            title_objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            title_objective.setDisplayName("C");
            Team team = sb.registerNewTeam("T");
            team.addEntry("ENT-TEST");
            team.addEntry("ENT-TEST-2");
            team.setPrefix("TEAM-PREFIX");
            title_objective.getScore("ENT-TEST").setScore(0);
            title_objective.getScore("ENT-TEST-2").setScore(123456);
        }

        // spawn everyone
        double spawnCenterX = plugin.getConfig().getDouble("battles.spawn.center-x");
        double spawnCenterZ = plugin.getConfig().getDouble("battles.spawn.center-x");
        double spawnRadius = plugin.getConfig().getDouble("battles.spawn.radius");
        Random r = new Random();
        for(Player p : plugin.getServer().getOnlinePlayers()) {
            double x = spawnCenterX + (r.nextDouble() * spawnRadius * (r.nextBoolean() ? 1d : -1d));
            double z = spawnCenterZ + (r.nextDouble() * spawnRadius * (r.nextBoolean() ? 1d : -1d));
            double y = 192d + (r.nextDouble() * 32d);
            Location location = new Location(world, x, y, z);
            p.teleport(location);
            p.setScoreboard(sb);
            plugin.getServer().getScheduler().runTask(plugin, () ->
                plugin.getServer().getPluginManager().callEvent(new PlayerInteractEvent(
                        p,
                        Action.RIGHT_CLICK_AIR,
                        ParachuteManager.getParachuteItem(plugin.getConfig().getString("battles.parachute-type")),
                        null,
                        null
                ))
            );
        }

        // set world border
        WorldBorder border = world.getWorldBorder();
        border.setCenter(
                plugin.getConfig().getInt("battle-world.center-x"),
                plugin.getConfig().getInt("battle-world.center-z")
        );
        border.setDamageAmount(10000d);
        border.setSize(plugin.getConfig().getDouble("battles.safe-zone.initial-size"));
        border.setSize(100, 200);
    }

    public void endBattles() {
        if(!started) {
            throw new IllegalStateException();
        }
        started = false;
        plugin.setEndPhase();
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
