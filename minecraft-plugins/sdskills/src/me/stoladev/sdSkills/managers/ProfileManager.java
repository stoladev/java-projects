package me.stoladev.sdSkills.managers;

import me.stoladev.sdSkills.Main;
import me.stoladev.sdSkills.components.Profile;
import me.stoladev.sdSkills.components.Skills;
import me.stoladev.sdSkills.configs.ProfileConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static me.stoladev.sdSkills.Utils.log;

public class ProfileManager {

  private final Map<String, Profile> profiles = new HashMap<>();

  private final FileConfiguration config;

  public ProfileManager(Main main) {
    ProfileConfig profileConfig = main.getProfileConfig();
    config = profileConfig.getConfig();
  }

  public void loadProfilesFromConfig() {
    for (String id : Objects.requireNonNull(config.getConfigurationSection("")).getKeys(false)) {

      int combatExperience = config.getInt(id + ".world.skills.combatExperience");
      int miningExperience = config.getInt(id + ".world.skills.miningExperience");
      int woodcuttingExperience = config.getInt(id + ".world.skills.woodcuttingExperience");
      int farmingExperience = config.getInt(id + ".world.skills.farmingExperience");
      int agilityExperience = config.getInt(id + ".world.skills.agilityExperience");

      int totalExperience =
          combatExperience
              + miningExperience
              + woodcuttingExperience
              + farmingExperience
              + agilityExperience;

      Skills skills =
          new Skills(
              totalExperience,
              combatExperience,
              miningExperience,
              woodcuttingExperience,
              farmingExperience,
              agilityExperience);

      Profile profile = new Profile(skills);
      profiles.put(id, profile);
      log("Loaded profile skills for " + id + ".");
    }
  }

  public void saveProfilesToConfig() {
    for (String id : profiles.keySet()) {
      Profile profile = profiles.get(id);
      Skills skills = profile.getSkills();

      config.set(id + ".world.skills.totalExperience", skills.getXP("Total"));
      config.set(id + ".world.skills.combatExperience", skills.getXP("Combat"));
      config.set(id + ".world.skills.miningExperience", skills.getXP("Mining"));
      config.set(id + ".world.skills.woodcuttingExperience", skills.getXP("Woodcutting"));
      config.set(id + ".world.skills.farmingExperience", skills.getXP("Farming"));
      config.set(id + ".world.skills.agilityExperience", skills.getXP("Agility"));

      log("Saved profile skills for " + id + ".");
    }
  }

  public Profile createNewProfile(Player player) {

    int startingCombatExperience = 0;
    int startingMiningExperience = 0;
    int startingFarmingExperience = 0;
    int startingWoodcuttingExperience = 0;
    int startingAgilityExperience = 0;
    int startingTotalExperience = 0;

    Skills skills =
        new Skills(
            startingTotalExperience,
            startingCombatExperience,
            startingMiningExperience,
            startingWoodcuttingExperience,
            startingFarmingExperience,
            startingAgilityExperience);

    Profile profile = new Profile(skills);
    String id = player.getUniqueId() + player.getWorld().getName();
    profiles.put(id, profile);
    return profile;
  }

  public String getProfileId(Player player) {

    String worldName;
    boolean worldIsNether = player.getWorld().getName().endsWith("_nether");
    boolean worldIsTheEnd = player.getWorld().getName().endsWith("_the_end");

    if (worldIsNether) {
      worldName = player.getWorld().getName().replace("_nether", "");
      String id = player.getUniqueId() + worldName;
      return player.getUniqueId() + worldName;
    }

    if (worldIsTheEnd) {
      worldName = player.getWorld().getName().replace("_the_end", "");
      String id = player.getUniqueId() + worldName;
      return player.getUniqueId() + worldName;
    }

    worldName = player.getWorld().getName();
    return player.getUniqueId() + worldName;
  }

  public Profile getPlayerProfile(String id) {
    return profiles.get(id);
  }
}
