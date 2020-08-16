package me.stoladev.sdSkills.managers;

import me.stoladev.sdSkills.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.List;
import java.util.Objects;

import static me.stoladev.sdSkills.Utils.log;
import static org.bukkit.Bukkit.getScoreboardManager;

public class OverlayManager {

  private final Main main;
  private ScoreboardManager scoreboardManager;
  private Scoreboard generalOverlay;
  private Scoreboard raidOverlay;
  private Objective sidebar;
  private Objective tabList;
  private Objective belowName;
  private boolean inRaid;
  private Team raiderTeam;

  public OverlayManager(Main main) {
    this.main = main;
  }

  public void setScoreboardManager(ScoreboardManager scoreboardManager) {
    this.scoreboardManager = scoreboardManager;
  }

  public Objective getTabList() {
    return tabList;
  }

  public void setTabList(Objective tabList) {
    this.tabList = tabList;
  }

  public Objective getBelowName() {
    return belowName;
  }

  public void setBelowName(Objective belowName) {
    this.belowName = belowName;
  }

  public Objective getSidebar() {
    return sidebar;
  }

  public void setSidebar(Objective sidebar) {
    this.sidebar = sidebar;
  }

  public void loadGeneralOverlay(Player player) {

    scoreboardManager = getScoreboardManager();
    generalOverlay = Objects.requireNonNull(scoreboardManager).getMainScoreboard();

    if (generalOverlay.getObjective(DisplaySlot.PLAYER_LIST) == null) {

      tabList = generalOverlay.registerNewObjective("playerListHealth", "health", "List Health");
      tabList.setDisplaySlot(DisplaySlot.PLAYER_LIST);
      tabList.setRenderType(RenderType.HEARTS);
    }

    if (generalOverlay.getObjective(DisplaySlot.BELOW_NAME) == null) {

      belowName = generalOverlay.registerNewObjective("belowNameHealth", "health", "Name Health");
      belowName.setDisplaySlot(DisplaySlot.BELOW_NAME);
    }

    player.setScoreboard(generalOverlay);
  }

  public void loadRaidOverlay(List<Player> Player) {

    scoreboardManager = getScoreboardManager();

    for (Player player : Player) {

      if (raidOverlay == null) {
        raidOverlay = Objects.requireNonNull(scoreboardManager).getNewScoreboard();
      }

      if (raidOverlay.getTeam("Raiders") == null) {
        raiderTeam = raidOverlay.registerNewTeam("Raiders");
        raiderTeam.setPrefix("Hero ");
        raiderTeam.setColor(ChatColor.GOLD);
      }

      if (raiderTeam == null) {
        raiderTeam = raidOverlay.getTeam("Raiders");
      }

      if (Objects.requireNonNull(raiderTeam).hasEntry(player.getDisplayName())) {
        return;
      }

      Objects.requireNonNull(raiderTeam).addEntry(player.getDisplayName());

      if (sidebar == null) {
        sidebar =
            Objects.requireNonNull(raiderTeam.getScoreboard())
                .registerNewObjective("raidHealth", "health", "Player's Health");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
      }

      player.setScoreboard(raidOverlay);
      log("Raid overlay set for player " + player);
    }
  }

  public void unloadRaidOverlay(List<Player> Players) {

    for (Player player : Players) {
      if (player.getScoreboard() != raidOverlay) {
        return;
      }

      loadGeneralOverlay(player);
      log("General overlay set for " + player.getDisplayName());
    }

    if (raiderTeam != null) {
      raiderTeam.unregister();
    }
  }
}
