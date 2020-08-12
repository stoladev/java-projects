package me.stoladev.sdSkills.commands;

import me.stoladev.sdSkills.Main;
import me.stoladev.sdSkills.components.Skills;
import me.stoladev.sdSkills.managers.ExperienceManager;
import me.stoladev.sdSkills.managers.ProfileManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static me.stoladev.sdSkills.Utils.*;

public class GiveExperienceCommand implements TabExecutor {

  private final Main main;
  private final ProfileManager profileManager;
  private final ExperienceManager experienceManager;

  public GiveExperienceCommand(Main main) {
    this.profileManager = main.getProfileManager();
    this.experienceManager = main.getExperienceManager();
    this.main = main;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    if (!(sender instanceof Player)) {
      log("This feature is allowed for players only.");
      return true;
    }

    Player givingPlayer = (Player) sender;

    if (!givingPlayer.hasPermission("sdSkills.admin")) {
      givingPlayer.sendMessage(color("&cYou are not allowed to use this feature."));
      return true;
    }

    if (args[0].length() == 0 || args[1].length() == 0) {
      givingPlayer.sendMessage(color("&4 Wrong format. /givemining PlayerName XPToGive"));
      return true;
    }

    int addedXP = Integer.parseInt(args[1]);
    String skillName;
    Player receivingPlayer = main.getServer().getPlayer(args[0]);
    assert receivingPlayer != null;

    String id = profileManager.getProfileId(receivingPlayer);
    Skills skills = profileManager.getPlayerProfile(id).getSkills();

    String receivingName = sdLabelAdmin + " &f" + receivingPlayer.getDisplayName();
    String givingName = sdLabel + " &f" + givingPlayer.getDisplayName();

    switch (label) {
      case "givemining":
        skillName = "Mining";
        break;

      case "givefarming":
        skillName = "Farming";
        break;

      case "givecombat":
        skillName = "Combat";
        break;

      case "giveagility":
        skillName = "Agility";
        break;

      default:
        return true;
    }

    experienceManager.giveExperience(receivingPlayer, skills, skillName, addedXP);

    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
    String formattedXP = numberFormat.format(addedXP);

    // Message to giver
    givingPlayer.sendMessage(color(receivingName + " has received " + formattedXP + "XP."));

    // Message to receiver
    receivingPlayer.sendMessage(color(givingName + " has given you " + formattedXP + "XP."));

    return true;
  }

  @Override
  public List<String> onTabComplete(
      CommandSender sender, Command command, String label, String[] args) {
    return null;
  }
}
