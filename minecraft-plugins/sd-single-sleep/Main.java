package me.stoladev.sdSingleSleep;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

  // TODO Make sure player stays in bed for entire sleep cycle.

  public int transitionTask = 1;
  public long pTime = 0;

  // mobs stop spawning at: 22813
  // mobs start to burn at: 23600
  private long mobSpawningStartTime = 13000;
  private long mobSpawningStopTime = 23600;
  private boolean isCancelled;

  @Override
  public void onEnable() {
    getServer().getPluginManager().registerEvents(this, this);
  }

  @Override
  public void onDisable() {}

  @EventHandler
  public void PlayerEnteredBed(PlayerBedEnterEvent event) throws InterruptedException {
    final Player player = event.getPlayer();
    final World world = player.getWorld();


    if (IsNight(world) || world.isThundering()) {

      isCancelled = false;

      setDaytime(world);


    } else {
      player.setBedSpawnLocation(event.getBed().getLocation());
    }
  }

  @EventHandler
  public void PlayerLeftBed(PlayerBedLeaveEvent event) {
    List<World> worlds = Bukkit.getWorlds();
    final Player player = event.getPlayer();
    final World world = player.getWorld();

  }

  public void setDaytime(World world) {
    if (world.hasStorm()) {
      world.setStorm(false);
    }

    if (world.isThundering()) {
      world.setThundering(false);
    }

    long Relative_Time = 24000 - world.getTime();
    world.setFullTime(world.getFullTime() + Relative_Time);
  }

  public boolean IsNight(World world) {

    long time = (world.getFullTime()) % 24000;
    return time >= mobSpawningStartTime && time < mobSpawningStopTime;
  }
}
