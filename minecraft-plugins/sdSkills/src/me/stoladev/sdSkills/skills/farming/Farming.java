package me.stoladev.sdSkills.skills.farming;

import me.stoladev.sdSkills.Main;
import me.stoladev.sdSkills.components.Skills;
import me.stoladev.sdSkills.managers.ExperienceManager;
import me.stoladev.sdSkills.managers.ProfileManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import static me.stoladev.sdSkills.Utils.color;

public class Farming {

  private final ProfileManager profileManager;
  private final ExperienceManager experienceManager;
  private final Main main;

  public Farming(Main main) {
    this.profileManager = main.getProfileManager();
    this.experienceManager = main.getExperienceManager();
    this.main = main;
  }

  public boolean onFarm(BlockBreakEvent event) {

    Block block = event.getBlock();
    int levelRequired;
    int experienceGained;

    switch (block.getType()) {
      case WHEAT:
      case SUGAR_CANE:
        levelRequired = 1;
        experienceGained = 3;
        break;

      case CARROTS:
      case BEETROOTS:
        levelRequired = 5;
        experienceGained = 10;
        break;

      case POTATOES:
        levelRequired = 10;
        experienceGained = 25;
        break;

      case MELON:
        levelRequired = 20;
        experienceGained = 75;
        break;

      case PUMPKIN:
        levelRequired = 40;
        experienceGained = 150;
        break;

      case COCOA:
        levelRequired = 50;
        experienceGained = 300;
        break;

      default:
        return false;
    }

    Player player = event.getPlayer();
    String id = profileManager.getProfileId(player);
    Skills skills = profileManager.getPlayerProfile(id).getSkills();

    if (!canFarmCrop(player, skills, block, levelRequired, experienceGained)) {
      event.setCancelled(true);
      return true;
    }

    return true;
  }

  private boolean canFarmCrop(
      Player player, Skills skills, Block block, int levelRequired, int experienceGained) {

    boolean inWorld2 = player.getWorld().getName().contains("world2");
    boolean canFarmCrop = levelRequired <= skills.getLevel("Farming");
    if (canFarmCrop || inWorld2) {
      if (isFullGrown(block)) {
        experienceManager.giveExperience(player, skills, "Farming", experienceGained);
      }
      return true;
    }

    String msg = "&4You need " + levelRequired + " Farming to farm this crop.";
    player.sendMessage(color(msg));
    return false;
  }

  private boolean isFullGrown(Block block) {

    if (block.getType() == Material.PUMPKIN || block.getType() == Material.MELON) {
      return true;
    }

    org.bukkit.block.data.Ageable age = ((org.bukkit.block.data.Ageable) block.getBlockData());

    return age.getAge() >= age.getMaximumAge();
  }
}
