package me.stoladev.sdSkills;

import me.stoladev.sdSkills.configs.ProfileConfig;
import me.stoladev.sdSkills.listeners.*;
import me.stoladev.sdSkills.managers.CommandManager;
import me.stoladev.sdSkills.managers.ExperienceManager;
import me.stoladev.sdSkills.managers.OverlayManager;
import me.stoladev.sdSkills.managers.ProfileManager;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.logging.Logger;

import static me.stoladev.sdSkills.Utils.log;

public class Main extends JavaPlugin {

  public static Random randomNumberGenerator = new Random();
  private static Logger logger;

  private ProfileManager profileManager;
  private ProfileConfig profileConfig;
  private OverlayManager overlayManager;
  private ExperienceManager experienceManager;
  private CommandManager commandManager;

  public static Logger getPluginLogger() {
    return logger;
  }

  @Override
  public void onEnable() {
    logger = getLogger();

    enableSkills();

    // General
    commandManager = new CommandManager(this);
    commandManager.loadCommands();
    overlayManager = new OverlayManager(this);

    new LoginListener(this);
    new RaidListener(this);

    log("SDSkills enabled.");
  }

  @Override
  public void onDisable() {
    // Save the profiles to config BEFORE saving the config to the file.
    profileManager.saveProfilesToConfig();
    profileConfig.saveConfig();

    log("SDSkills disabled.");
  }

  private void enableSkills() {

    LootTables woodlandMansionTable = LootTables.WOODLAND_MANSION;
    LootTable woodlandMansion = LootTables.WOODLAND_MANSION.getLootTable();

    profileConfig = new ProfileConfig(this, "profiles");

    profileConfig.loadConfig();
    profileManager = new ProfileManager(this);
    profileManager.loadProfilesFromConfig();

    experienceManager = new ExperienceManager(this);

    new InventoryClickListener(this);
    new BlockBreakListener(this);
    new BlockPlaceListener(this);
    new GrowthListener(this);
    new CombatListener(this);
    new MovementListener(this);
  }

  public ProfileConfig getProfileConfig() {
    return profileConfig;
  }

  public OverlayManager getOverlayManager() {
    return overlayManager;
  }

  public ProfileManager getProfileManager() {
    return profileManager;
  }

  public ExperienceManager getExperienceManager() {
    return experienceManager;
  }
}
