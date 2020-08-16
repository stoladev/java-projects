package me.stoladev.sdSkills.commands;

import me.stoladev.sdSkills.Main;
import me.stoladev.sdSkills.components.Skills;
import me.stoladev.sdSkills.managers.ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static me.stoladev.sdSkills.Utils.color;
import static me.stoladev.sdSkills.Utils.log;

public class ChatLevelCommand implements TabExecutor {

  private final ProfileManager profileManager;

  public ChatLevelCommand(Main main) {
    this.profileManager = main.getProfileManager();
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    if (!(sender instanceof Player)) {
      log("This feature is allowed for players only.");
      return true;
    }

    Player player = (Player) sender;
    String id = profileManager.getProfileId(player);
    Skills skills = profileManager.getPlayerProfile(id).getSkills();
    String pName = player.getDisplayName();
    String skillName;
    String msg;
    String msg2;

    int skillLevel;
    int skillXP;

    switch (label) {
      case "chattotal":
        skillName = "Total";
        skillLevel = skills.getTotalLevel();
        skillXP = skills.getXP("Total");
        break;

      case "chatcombat":
        skillName = "Combat";
        skillLevel = skills.getLevel("Combat");
        skillXP = skills.getXP("Combat");
        break;

      case "chatmining":
        skillName = "Mining";
        skillLevel = skills.getLevel("Mining");
        skillXP = skills.getXP("Mining");
        break;

      case "chatfarming":
        skillName = "Farming";
        skillLevel = skills.getLevel("Farming");
        skillXP = skills.getXP("Farming");
        break;

      case "chatagility":
        skillName = "Agility";
        skillLevel = skills.getLevel("Agility");
        skillXP = skills.getXP("Agility");
        break;

      default:
        return true;
    }

    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
    String formattedXP = numberFormat.format(skillXP);

    msg = pName + "'s " + skillName + " Level is " + skillLevel + "; ";
    msg2 = "they have " + formattedXP + " XP.";

    Bukkit.broadcastMessage(color("&8[sdSkills] " + "&f" + msg + msg2));

    return true;
  }

  @Override
  public List<String> onTabComplete(
      CommandSender sender, Command command, String label, String[] args) {
    return null;
  }
}
