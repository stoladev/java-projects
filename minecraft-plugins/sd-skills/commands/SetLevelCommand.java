package me.stoladev.sdSkills.commands;

import me.stoladev.sdSkills.Main;
import me.stoladev.sdSkills.components.Skills;
import me.stoladev.sdSkills.managers.ProfileManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

import static me.stoladev.sdSkills.Utils.*;

public class SetLevelCommand implements TabExecutor {

  private final Main main;
  private final ProfileManager profileManager;

  public SetLevelCommand(Main main) {
    this.profileManager = main.getProfileManager();
    this.main = main;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    if (!(sender instanceof Player)) {
      log("This feature is allowed for players only.");
      return true;
    }

    Player settingPlayer = (Player) sender;
    int level = Integer.parseInt(args[1]);
    String skillName;
    String setterMsg;
    String receiverMsg;
    Player receivingPlayer = main.getServer().getPlayer(args[0]);
    assert receivingPlayer != null;

    String id = profileManager.getProfileId(receivingPlayer);
    Skills skills = profileManager.getPlayerProfile(id).getSkills();

    String receivingName = sdLabelAdmin + " &f" + receivingPlayer.getDisplayName();
    String settingName = sdLabel + " &f" + settingPlayer.getDisplayName();

    switch (label) {
      case "setmining":
        skillName = "Mining";
        break;

      case "setfarming":
        skillName = "Farming";
        break;

      case "setcombat":
        skillName = "Combat";
        break;

      case "setagility":
        skillName = "Agility";
        break;

      default:
        return true;
    }

    skills.setLevel(skills, skillName, level);
    receivingPlayer.sendMessage(sdLabel + " Your " + skillName + " has been set to " + level + ".");

    // Message to setter
    setterMsg = skillName + " Level is now " + level + ".";
    settingPlayer.sendMessage(color(receivingName + "'s " + setterMsg));

    // Message to receiver
    receiverMsg = skillName + " Level to " + level + ".";
    receivingPlayer.sendMessage(color(settingName + " has set your " + receiverMsg));

    return true;
  }

  @Override
  public List<String> onTabComplete(
      CommandSender sender, Command command, String label, String[] args) {
    return null;
  }
}
