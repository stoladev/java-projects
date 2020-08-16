package me.stoladev.sdSkills.configs;

import me.stoladev.sdSkills.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static me.stoladev.sdSkills.Utils.error;
import static me.stoladev.sdSkills.Utils.log;

public class ProfileConfig {

  private final Main main;
  private final String fileName;
  private final File file;
  private final FileConfiguration config = new YamlConfiguration();

  public ProfileConfig(Main main, String fileName) {
    this.main = main;
    this.fileName = fileName;
    file = new File(main.getDataFolder(), fileName + ".yml");
  }

  public void loadConfig() {
    if (!file.exists()) {
      // If the file doesn't exist...
      file.getParentFile().mkdirs();
      main.saveResource(fileName + ".yml", false);
      log("Created profile config file.");
    } else {
      log("Found existing profile config file.");
    }
    try {
      config.load(file);
      log("Loaded profile config file.");
    } catch (IOException | InvalidConfigurationException exception) {
      error("Error while loading profile config file.");
      exception.printStackTrace();
    }
  }

  public FileConfiguration getConfig() {
    return config;
  }

  public void saveConfig() {
    try {
      config.save(file);
      log("Saved profile config file.");
    } catch (IOException exception) {
      error("Error while saving profile config file.");
      exception.printStackTrace();
    }
  }
}
