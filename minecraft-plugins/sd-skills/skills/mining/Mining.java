package me.stoladev.sdSkills.skills.mining;

import me.stoladev.sdSkills.Main;
import me.stoladev.sdSkills.components.Skills;
import me.stoladev.sdSkills.managers.ExperienceManager;
import me.stoladev.sdSkills.managers.ProfileManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import static me.stoladev.sdSkills.Utils.color;

public class Mining {

  private final ProfileManager profileManager;
  private final ExperienceManager experienceManager;

  public Mining(Main main) {
    this.profileManager = main.getProfileManager();
    this.experienceManager = main.getExperienceManager();
  }

  public boolean onMine(BlockBreakEvent event) {

    Block block = event.getBlock();

    int levelRequired;
    int experienceGained;

    switch (block.getType()) {
      case STONE:
      case NETHERRACK:
        levelRequired = 1;
        experienceGained = 1;
        break;

      case DIORITE:
      case GRANITE:
      case ANDESITE:
      case SANDSTONE:
        levelRequired = 1;
        experienceGained = 5;
        break;

      case BLACKSTONE:
      case BASALT:
      case NETHER_BRICK:
        levelRequired = 5;
        experienceGained = 15;
        break;

      case COAL_ORE:
        levelRequired = 10;
        experienceGained = 25;
        break;

      case IRON_ORE:
        levelRequired = 15;
        experienceGained = 50;
        break;

      case GOLD_ORE:
        levelRequired = 25;
        experienceGained = 75;
        break;

      case LAPIS_ORE:
      case REDSTONE_ORE:
      case NETHER_QUARTZ_ORE:
        levelRequired = 30;
        experienceGained = 100;
        break;

      case NETHER_GOLD_ORE:
      case GILDED_BLACKSTONE:
      case DIAMOND_ORE:
        levelRequired = 40;
        experienceGained = 250;
        break;

      case EMERALD_ORE:
        levelRequired = 50;
        experienceGained = 500;
        break;

      case ANCIENT_DEBRIS:
        levelRequired = 60;
        experienceGained = 1000;
        break;

      default:
        return false;
    }

    Player player = event.getPlayer();
    String id = profileManager.getProfileId(player);
    Skills skills = profileManager.getPlayerProfile(id).getSkills();

    if (!canUsePickaxe(player, skills)) {
      event.setCancelled(true);
      return true;
    }

    if (!canMineOre(player, skills, levelRequired, experienceGained)) {
      event.setCancelled(true);
      return true;
    }

    return true;
  }

  private boolean canUsePickaxe(Player player, Skills skills) {

    // Disable check on World 2.
    boolean inWorld2 = player.getWorld().getName().contains("world2");
    if (inWorld2) {
      return true;
    }

    Material pickaxe = player.getInventory().getItemInMainHand().getType();
    String itemName;
    int levelRequired;

    switch (pickaxe) {
      case IRON_PICKAXE:
        itemName = "an Iron Pickaxe";
        levelRequired = 15;
        break;
      case GOLDEN_PICKAXE:
        itemName = "a Golden Pickaxe";
        levelRequired = 25;
        break;
      case DIAMOND_PICKAXE:
        itemName = "a Diamond Pickaxe";
        levelRequired = 40;
        break;
      case NETHERITE_PICKAXE:
        itemName = "a Netherite Pickaxe";
        levelRequired = 60;
        break;
      default:
        return true;
    }

    boolean canUsePickaxe = skills.getLevel("Mining") >= levelRequired;
    if (canUsePickaxe) {
      return true;
    }

    String msg = "&4You need " + levelRequired + " Mining to use " + itemName + ".";
    player.sendMessage(color(msg));
    return false;
  }

  private boolean canMineOre(
      Player player, Skills skills, int levelRequired, int experienceGained) {

    // Disable check on World 2, or if player has the mining level.
    boolean inWorld2 = player.getWorld().getName().contains("world2");
    boolean canMine = skills.getLevel("Mining") >= levelRequired;
    if (inWorld2 || canMine) {
      experienceManager.giveExperience(player, skills, "Mining", experienceGained);
      return true;
    }

    String msg = "&4You need " + levelRequired + " Mining to mine this.";
    player.sendMessage(color(msg));
    return false;
  }
}
