package me.stoladev.sdSkills.commands;

import me.stoladev.sdSkills.Main;
import me.stoladev.sdSkills.components.Skills;
import me.stoladev.sdSkills.managers.ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static me.stoladev.sdSkills.Utils.color;
import static me.stoladev.sdSkills.Utils.log;

public class SkillCommand implements TabExecutor {

  private final ProfileManager profileManager;

  private final ItemStack totalLevelItem;
  private final ItemStack combatItem;
  private final ItemStack miningItem;
  private final ItemStack woodcuttingItem;
  private final ItemStack farmingItem;
  private final ItemStack agilityItem;

  public SkillCommand(Main main) {
    profileManager = main.getProfileManager();

    totalLevelItem = new ItemStack(Material.KNOWLEDGE_BOOK, 1);
    ItemMeta totalLevelItemMeta = totalLevelItem.getItemMeta();
    Objects.requireNonNull(totalLevelItemMeta).addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    Objects.requireNonNull(totalLevelItemMeta).setDisplayName(color("&eTotal Level"));
    totalLevelItem.setItemMeta(totalLevelItemMeta);

    combatItem = new ItemStack(Material.NETHERITE_SWORD, 1);
    ItemMeta combatItemMeta = combatItem.getItemMeta();
    Objects.requireNonNull(combatItemMeta).addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    Objects.requireNonNull(combatItemMeta).setDisplayName(color("&eCombat"));
    combatItem.setItemMeta(combatItemMeta);

    miningItem = new ItemStack(Material.NETHERITE_PICKAXE, 1);
    ItemMeta miningItemMeta = miningItem.getItemMeta();
    Objects.requireNonNull(miningItemMeta).addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    Objects.requireNonNull(miningItemMeta).setDisplayName(color("&eMining"));
    miningItem.setItemMeta(miningItemMeta);

    woodcuttingItem = new ItemStack(Material.NETHERITE_AXE, 1);
    ItemMeta woodcuttingItemMeta = woodcuttingItem.getItemMeta();
    Objects.requireNonNull(woodcuttingItemMeta).addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    Objects.requireNonNull(woodcuttingItemMeta).setDisplayName(color("&eWoodcutting"));
    woodcuttingItem.setItemMeta(woodcuttingItemMeta);

    farmingItem = new ItemStack(Material.NETHERITE_HOE, 1);
    ItemMeta farmingItemMeta = farmingItem.getItemMeta();
    Objects.requireNonNull(farmingItemMeta).addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    Objects.requireNonNull(farmingItemMeta).setDisplayName(color("&eFarming"));
    farmingItem.setItemMeta(farmingItemMeta);

    agilityItem = new ItemStack(Material.NETHERITE_BOOTS, 1);
    ItemMeta agilityItemMeta = agilityItem.getItemMeta();
    Objects.requireNonNull(agilityItemMeta).addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    Objects.requireNonNull(agilityItemMeta).setDisplayName(color("&eAgility"));
    agilityItem.setItemMeta(agilityItemMeta);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String s, String[] strings) {

    if (!(sender instanceof Player)) {
      log("This feature is allowed for players only.");
      return true;
    }

    Player player = (Player) sender;
    String id = profileManager.getProfileId(player);
    Skills skills = profileManager.getPlayerProfile(id).getSkills();

    String GUITitle = "&l" + player.getDisplayName() + "'s Skills";

    Inventory skillsGUI = Bukkit.createInventory(null, 54, color(GUITitle));

    skillsGUI.setItem(8, editItem(totalLevelItem.clone(), hoverInformation("Total", skills)));
    skillsGUI.setItem(9, editItem(combatItem.clone(), hoverInformation("Combat", skills)));
    skillsGUI.setItem(18, editItem(miningItem.clone(), hoverInformation("Mining", skills)));
    skillsGUI.setItem(
        27, editItem(woodcuttingItem.clone(), hoverInformation("Woodcutting", skills)));
    skillsGUI.setItem(36, editItem(farmingItem.clone(), hoverInformation("Farming", skills)));
    skillsGUI.setItem(45, editItem(agilityItem.clone(), hoverInformation("Agility", skills)));

    player.openInventory(skillsGUI);

    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] strings) {
    return null;
  }

  private List<String> hoverInformation(String skillName, Skills skills) {

    int level;
    int currentXP;
    int xpTilLevel;

    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

    switch (skillName) {
      case "Total":
        level = skills.getTotalLevel();
        currentXP = skills.getXP("Total");
        break;

      case "Combat":
        level = skills.getLevel("Combat");
        currentXP = skills.getXP("Combat");
        break;

      case "Mining":
        level = skills.getLevel("Mining");
        currentXP = skills.getXP("Mining");
        break;

      case "Woodcutting":
        level = skills.getLevel("Woodcutting");
        currentXP = skills.getXP("Woodcutting");
        break;

      case "Farming":
        level = skills.getLevel("Farming");
        currentXP = skills.getXP("Farming");
        break;

      case "Agility":
        level = skills.getLevel("Agility");
        currentXP = skills.getXP("Agility");
        break;

      default:
        return null;
    }

    String currentLevel = "&fCurrent Level: " + level;
    String formattedCXP = "&FCurrent XP: " + numberFormat.format(currentXP);

    if (skillName.equalsIgnoreCase("Total")) {
      return Arrays.asList(color(currentLevel), color(formattedCXP));
    }

    xpTilLevel = skills.calculateXPToGoal(currentXP, (level + 1));
    String formattedXPTL = "&FXP 'til " + (level + 1) + ": " + numberFormat.format(xpTilLevel);

    return Arrays.asList(color(currentLevel), color(formattedCXP), "", color(formattedXPTL));
  }

  public ItemStack editItem(ItemStack item, List<String> lore) {
    item.setAmount(1);
    ItemMeta itemMeta = item.getItemMeta();
    Objects.requireNonNull(itemMeta).setLore(lore);
    item.setItemMeta(itemMeta);
    return item;
  }
}
