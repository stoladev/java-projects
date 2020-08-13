package me.stoladev.sdSkills.listeners;

import me.stoladev.sdSkills.Main;
import me.stoladev.sdSkills.managers.OverlayManager;
import me.stoladev.sdSkills.managers.ProfileManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Raid;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.raid.RaidFinishEvent;
import org.bukkit.event.raid.RaidSpawnWaveEvent;
import org.bukkit.event.raid.RaidStopEvent;
import org.bukkit.event.raid.RaidTriggerEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static me.stoladev.sdSkills.Main.randomNumberGenerator;
import static me.stoladev.sdSkills.Utils.log;
import static org.bukkit.Bukkit.getServer;

public class RaidListener implements Listener {

  public static Raid currentRaidTracker;
  public static int currentWaveNumber = 0;
  public static long currentRaidBeginTime;
  public static long currentRaidEndTime;
  public static boolean raidInProgress;

  private final Main main;
  private final OverlayManager overlayManager;
  private BukkitTask raidTask;
  private ProfileManager profileManager;

  public RaidListener(Main main) {
    main.getServer().getPluginManager().registerEvents(this, main);
    this.main = main;
    this.overlayManager = main.getOverlayManager();
  }

  @EventHandler()
  public void OnRaidStart(RaidTriggerEvent event) {

    if (raidInProgress) {
      event.setCancelled(true);
      return;
    }

    currentRaidTracker = event.getRaid();
    raidInProgress = true;
    currentWaveNumber = 0;
    currentRaidBeginTime = System.currentTimeMillis();

    Player player = event.getPlayer();
    Raid raid = event.getRaid();

    List<Entity> NearbyEntities = player.getNearbyEntities(200.0D, 200.0D, 200.0D);
    List<Player> NearbyPlayers = new ArrayList<>();

    for (Entity entity : NearbyEntities) {

      if (entity instanceof Player) {

        Player nearbyPlayer = (Player) entity;
        NearbyPlayers.add(nearbyPlayer);
        NearbyPlayers.add(player);
        overlayManager.loadRaidOverlay(NearbyPlayers);
        String msg = " has started a raid! Prepare yourselves!";
        nearbyPlayer.sendMessage(ChatColor.RED + player.getDisplayName() + ChatColor.WHITE + msg);
      }
    }

    String msg = "You've started a raid! Make sure you and your team is ready.";
    player.sendMessage(ChatColor.RED + msg);
  }

  @EventHandler()
  public void onRaidWave(RaidSpawnWaveEvent event) {
    currentWaveNumber = currentWaveNumber + 1;
    World w = event.getWorld();
    Location loc = event.getRaid().getLocation();

    spawnWaveExtraMobs(event, w, loc, currentWaveNumber);

    Set<UUID> Heroes = event.getRaid().getHeroes();
    List<Player> Players = new ArrayList<>();

    for (UUID hero : Heroes) {
      Player player = main.getServer().getPlayer(hero);
      Players.add(player);
    }

    overlayManager.loadRaidOverlay(Players);

    for (Player player : Players) {
      String msg = " has started!";
      player.sendMessage(ChatColor.RED + "Raid Wave " + currentWaveNumber + msg);
    }
  }

  @EventHandler()
  public void onRaidFinish(RaidFinishEvent event) {

    String status = event.getRaid().getStatus().toString();

    main.getServer()
        .broadcastMessage(ChatColor.RED + "The raid has ended with a status of: " + status + ".");

    List<Player> Winners = event.getWinners();

    overlayManager.unloadRaidOverlay(Winners);
    raidEnded(Winners, status);
  }

  // Raid stop is a cancelled raid for some other reason
  @EventHandler()
  public void onRaidStop(RaidStopEvent event) {

    String status = event.getReason().toString();
    Location loc = event.getRaid().getLocation();

    Set<UUID> Heroes = event.getRaid().getHeroes();
    List<Player> Players = new ArrayList<>();

    for (UUID hero : Heroes) {
      Player p = main.getServer().getPlayer(hero);
      Players.add(p);
    }

    overlayManager.unloadRaidOverlay(Players);
    raidEnded(Players, status);
  }

  public void raidEnded(List<Player> Players, String status) {
    // raidEnded can be called twice so don't run again if it's run already
    if (!raidInProgress) return;

    // cancel the listener timer
    if (raidTask != null) this.raidTask.cancel();

    // raid no longer in progress
    currentRaidTracker = null;
    raidInProgress = false;
    currentWaveNumber = 0;
    currentRaidEndTime = System.currentTimeMillis();

    long millis = currentRaidEndTime - currentRaidBeginTime;

    String hms =
        String.format(
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));

    for (Player player : Players) {
      player.sendMessage(ChatColor.RED + "The raid's length was " + hms + ".");
    }

    overlayManager.unloadRaidOverlay(Players);
  }

  public Location getRandomLocNearby(World w, Location loc, int d) {
    int x = loc.getBlockX();
    int z = loc.getBlockZ();

    int xr = randomNumberGenerator.nextInt(d);
    int zr = randomNumberGenerator.nextInt(d);
    int ixr = randomNumberGenerator.nextInt(d);
    int izr = randomNumberGenerator.nextInt(d);

    int fxr = x + (xr - ixr);
    int fzr = z + (zr - izr);

    Block b = Objects.requireNonNull(loc.getWorld()).getHighestBlockAt(fxr, fzr);

    return b.getLocation();
  }

  public void spawnIllusioner(World w, Location loc) {
    Location newLoc = getRandomLocNearby(w, loc, 50);

    Illusioner ill = (Illusioner) w.spawnEntity(newLoc, EntityType.ILLUSIONER);

    List<Entity> near = ill.getNearbyEntities(50.0D, 50.0D, 50.0D);

    for (Entity entity : near) {
      if (entity instanceof Player) {
        Player nearPlayer = (Player) entity;
        ill.setTarget(nearPlayer);
      }
    }
  }

  public void spawnWitherJockey(World world, Location loc) {
    Location newLoc = getRandomLocNearby(world, loc, 40);

    Spider spider = (Spider) world.spawnEntity(newLoc, EntityType.SPIDER);
    Husk husk = (Husk) world.spawnEntity(newLoc, EntityType.HUSK);
    spider.addPassenger(husk);

    // target someone
    List<Entity> near = husk.getNearbyEntities(75.0D, 75.0D, 75.0D);
    for (Entity entity : near) {
      if (entity instanceof Player) {
        Player nearPlayer = (Player) entity;
        husk.setTarget(nearPlayer);
      }
    }
  }

  public void spawnMob(String mob, int quantity, World world, Location location) {
    if (mob.equals("Wither Jockey")) {
      for (int i = 0; i < quantity; i++) {
        spawnWitherJockey(world, location);
      }
    } else if (mob.equals("Illusioner")) {
      for (int i = 0; i < quantity; i++) {
        spawnIllusioner(world, location);
      }
    } else {
      log("sdRaid: " + mob + "is not a valid mob.");
    }
  }

  public void spawnWaveExtraMobs(
      RaidSpawnWaveEvent event, World world, Location location, int wave) {
    // TODO: how many players in the raid (amplify raid when more people)
    // int p = PwnRaid.currentRaidTracker.getHeroes().size();
    // TODO: multiplier is players times the current wave.
    // int x = (wave * p);

    if (wave == 3) {
      spawnMob("Skeleton Jockey", 3, world, location);

    } else if (wave == 4) {
      spawnMob("Illusioner", 1, world, location);

    } else if (wave == 5) {
      spawnMob("Illusioner", 2, world, location);

    } else if (wave == 6) {
      spawnMob("Skeleton Jockey", 8, world, location);
      spawnMob("Illusioner", 2, world, location);

    } else if (wave == 7) {
      spawnMob("Illusioner", 3, world, location);

    } else if (wave >= 8) {
      spawnMob("Skeleton Jockey", 10, world, location);
      spawnMob("Illusioner", 3, world, location);
    }

    // Spider Riding Skeleton
    if (wave == 3 || wave == 6 || wave >= 8) {
      String msg = "The stench of The Undead fills your nostrils.";
      getServer().broadcastMessage(ChatColor.ITALIC + msg);
    }

    // Illusion Mob
    if (wave == 5 || wave == 6 || wave >= 7) {
      String msg = "You feel an eerie, dream-like presence in the air...";
      getServer().broadcastMessage(ChatColor.ITALIC + msg);
    }
  }
}
