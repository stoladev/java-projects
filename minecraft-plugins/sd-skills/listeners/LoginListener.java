package me.stoladev.sdSkills.listeners;

import me.stoladev.sdSkills.Main;
import me.stoladev.sdSkills.components.Profile;
import me.stoladev.sdSkills.managers.OverlayManager;
import me.stoladev.sdSkills.managers.ProfileManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import static me.stoladev.sdSkills.Utils.*;

public class LoginListener implements Listener {

  private final Main main;
  private final ProfileManager profileManager;
  private final OverlayManager overlayManager;

  public LoginListener(Main main) {
    main.getServer().getPluginManager().registerEvents(this, main);
    this.main = main;
    profileManager = main.getProfileManager();
    profileManager.saveProfilesToConfig();
    overlayManager = main.getOverlayManager();
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    String worldName = player.getWorld().getName();

    loadPlayerSkills(player);
    overlayManager.loadGeneralOverlay(player);

    player.sendMessage(color("&9Latest updates:"));
    player.sendMessage(color("&3Re-placed blocks can be mined by anyone, regardless of level."));
    player.sendMessage(color("&3However, they will disintegrate and not drop anything."));
    player.sendMessage(
        color("&3This prevents entombing players with low levels, yet allows decoration."));

    for (Player onlinePlayer : main.getServer().getOnlinePlayers()) {
      Sound sound = Sound.BLOCK_NOTE_BLOCK_BELL;
      onlinePlayer.playSound(onlinePlayer.getLocation(), sound, 1, 1);
    }
  }

  @EventHandler
  public void onPlayerLeave(PlayerLoginEvent event) {

    for (Player onlinePlayer : main.getServer().getOnlinePlayers()) {
      Sound sound = Sound.BLOCK_NOTE_BLOCK_BELL;
      onlinePlayer.playSound(onlinePlayer.getLocation(), sound, 1, 0.5f);
    }
  }

  @EventHandler
  public void onWorldChange(PlayerChangedWorldEvent event) {
    Player player = event.getPlayer();
    loadPlayerSkills(player);
  }

  public void loadPlayerSkills(Player player) {
    // Checks whether the player exists in the db.

    String id = profileManager.getProfileId(player);
    Profile profile = profileManager.getPlayerProfile(id);

    if (profile == null) {
      profileManager.createNewProfile(player);
      log(color(sdLabelAdmin + " NEW PLAYER " + player.getName() + " joined the server."));
    } else {
      log(color(sdLabelAdmin + " Player " + player.getName() + "'s profile already exists."));
    }
  }
}
