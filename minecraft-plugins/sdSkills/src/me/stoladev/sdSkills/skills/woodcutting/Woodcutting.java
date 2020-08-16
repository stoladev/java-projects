package me.stoladev.sdSkills.skills.woodcutting;

import me.stoladev.sdSkills.Main;
import me.stoladev.sdSkills.components.Skills;
import me.stoladev.sdSkills.managers.ExperienceManager;
import me.stoladev.sdSkills.managers.ProfileManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import static me.stoladev.sdSkills.Utils.color;

public class Woodcutting {

  private final Main main;

  private final ProfileManager profileManager;
  private final ExperienceManager experienceManager;

  public Woodcutting(Main main) {
    this.profileManager = main.getProfileManager();
    this.experienceManager = main.getExperienceManager();
    this.main = main;
  }

  public boolean onChop(BlockBreakEvent event) {

    Block block = event.getBlock();

    int levelRequired;
    int experienceGained;

    switch (block.getType()) {
      case ACACIA_LOG:
      case BIRCH_LOG:
      case JUNGLE_LOG:
      case OAK_LOG:
      case SPRUCE_LOG:
      case DARK_OAK_LOG:
        levelRequired = 1;
        experienceGained = 25;
        break;
      default:
        return false;
    }

    Player player = event.getPlayer();
    String id = profileManager.getProfileId(player);
    Skills skills = profileManager.getPlayerProfile(id).getSkills();

    if (!canUseAxe(player, skills)) {
      event.setCancelled(true);
      return true;
    }

    if (!canChopWood(player, skills, levelRequired, experienceGained)) {
      event.setCancelled(true);
      return true;
    }

    return true;
  }

  private boolean canUseAxe(Player player, Skills skills) {

    Material axe = player.getInventory().getItemInMainHand().getType();
    String itemName;
    int levelRequired;

    switch (axe) {
      case IRON_AXE:
        itemName = "an Iron Axe";
        levelRequired = 15;
        break;
      case GOLDEN_AXE:
        itemName = "a Golden Axe";
        levelRequired = 25;
        break;
      case DIAMOND_AXE:
        itemName = "a Diamond Axe";
        levelRequired = 40;
        break;
      case NETHERITE_AXE:
        itemName = "a Netherite Axe";
        levelRequired = 60;
        break;
      default:
        return true;
    }

    boolean canUseAxe = skills.getLevel("Woodcutting") >= levelRequired;
    boolean inWorld2 = player.getWorld().getName().contains("world2");
    if (canUseAxe || inWorld2) {
      return true;
    }

    String msg = "&4You need " + levelRequired + " Woodcutting to use " + itemName + ".";
    player.sendMessage(color(msg));
    return false;
  }

  private boolean canChopWood(
      Player player, Skills skills, int levelRequired, int experienceGained) {

    boolean canChop = skills.getLevel("Woodcutting") >= levelRequired;
    boolean inWorld2 = player.getWorld().getName().contains("world2");
    if (canChop || inWorld2) {
      experienceManager.giveExperience(player, skills, "Woodcutting", experienceGained);
      return true;
    }

    String msg = "&4You need " + levelRequired + " Woodcutting to chop this.";
    player.sendMessage(color(msg));
    return false;
  }
}
