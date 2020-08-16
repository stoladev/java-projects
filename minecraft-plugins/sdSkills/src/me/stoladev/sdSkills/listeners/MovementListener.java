package me.stoladev.sdSkills.listeners;

import me.stoladev.sdSkills.Main;
import me.stoladev.sdSkills.components.Skills;
import me.stoladev.sdSkills.managers.ExperienceManager;
import me.stoladev.sdSkills.managers.ProfileManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import static me.stoladev.sdSkills.Utils.*;
import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.FALL;

public class MovementListener implements Listener {

  private final ProfileManager profileManager;
  private final ExperienceManager experienceManager;

  public MovementListener(Main main) {
    main.getServer().getPluginManager().registerEvents(this, main);
    log(color(sdLabelAdmin + " Enabled Agility."));
    profileManager = main.getProfileManager();
    experienceManager = main.getExperienceManager();
  }

  @EventHandler
  public void onFallDamage(EntityDamageEvent event) {

    Entity damaged = event.getEntity();

    if (!(damaged instanceof Player)) {
      return;
    }

    Player player = (Player) damaged;

    String id = profileManager.getProfileId(player);
    Skills skills = profileManager.getPlayerProfile(id).getSkills();

    if (event.getCause() == FALL) {
      //      log("Damage: " + cause.getDamage());
      //      log("Final damage: " + cause.getFinalDamage());

      double damage = event.getDamage();
      int experienceGained = 25 * (int) damage;

      experienceManager.giveExperience(player, skills, "Agility", experienceGained);
    }
  }
}
