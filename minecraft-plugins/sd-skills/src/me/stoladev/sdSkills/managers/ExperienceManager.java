package me.stoladev.sdSkills.managers;

import me.stoladev.sdSkills.Main;
import me.stoladev.sdSkills.components.Skills;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static me.stoladev.sdSkills.Utils.color;

public class ExperienceManager {

  private final Main main;

  public ExperienceManager(Main main) {
    this.main = main;
  }

  public void giveExperience(Player player, Skills skills, String skillName, int experienceGained) {

    int levelBeforeExperienceGain;
    int levelAfterExperienceGain;

    levelBeforeExperienceGain = skills.getLevel(skillName);
    skills.setXP(skillName, skills.getXP(skillName) + experienceGained);
    levelAfterExperienceGain = skills.getLevel(skillName);

    if (levelAfterExperienceGain == levelBeforeExperienceGain) {
      return;
    }

    boolean broadcastUsed = false;

    boolean inWorld2 = player.getWorld().getName().contains("world2");

    if (inWorld2) {
      return;
    }

    for (Integer broadcastLevel : skills.getBroadcastLevels()) {
      if (levelAfterExperienceGain == broadcastLevel) {
        String levelAndSkill = broadcastLevel + " " + skillName + "!";
        String msg = player.getDisplayName() + " has achieved Level " + levelAndSkill;
        Bukkit.broadcastMessage(color("&6" + msg));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, .75f);

        for (Player onlinePlayer : main.getServer().getOnlinePlayers()) {
          Sound sound = Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR;
          onlinePlayer.playSound(onlinePlayer.getLocation(), sound, 1, .5f);
        }

        broadcastUsed = true;
      }
    }

    if (broadcastUsed) {
      return;
    }

    String skillAndLevel = skillName + " Level is now " + levelAfterExperienceGain + ".";
    String msg = "Congratulations! Your " + skillAndLevel;
    player.sendMessage(color("&e" + msg));
    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
  }
}
