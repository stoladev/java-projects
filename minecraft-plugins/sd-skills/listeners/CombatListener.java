package me.stoladev.sdSkills.listeners;

import me.stoladev.sdSkills.Main;
import me.stoladev.sdSkills.components.Skills;
import me.stoladev.sdSkills.managers.ProfileManager;
import me.stoladev.sdSkills.skills.combat.CombatData;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Objects;

import static me.stoladev.sdSkills.Utils.*;

public class CombatListener implements Listener {

  private final Main main;
  private final ProfileManager profileManager;
  private final CombatData combatData;

  public CombatListener(Main main) {
    main.getServer().getPluginManager().registerEvents(this, main);
    this.profileManager = main.getProfileManager();
    this.combatData = new CombatData();
    this.main = main;
    log(color(sdLabelAdmin + " Enabled Combat."));
  }

  @EventHandler
  public void OnCreatureKill(EntityDeathEvent event) {

    Player player = event.getEntity().getKiller();

    LivingEntity entity = event.getEntity();

    if (entity.getKiller() == null) {
      return;
    }

    if (entity.hasMetadata("NO_XP")) {
      return;
    }

    assert player != null;
    String id = profileManager.getProfileId(player);
    Skills skills = profileManager.getPlayerProfile(id).getSkills();
    boolean inWorld2 = event.getEntity().getWorld().getName().contains("world2");

    for (EntityType entityType : combatData.getLevel1Entities())
      if (inWorld2 || entity.getType() == entityType) {
        main.getExperienceManager().giveExperience(player, skills, "Combat", 50);
        return;
      }

    for (EntityType entityType : combatData.getLevel2Entities()) {
      if (inWorld2 || entity.getType() == entityType) {
        main.getExperienceManager().giveExperience(player, skills, "Combat", 100);
        return;
      }
    }

    for (EntityType entityType : combatData.getLevel3Entities()) {
      if (inWorld2 || entity.getType() == entityType) {
        main.getExperienceManager().giveExperience(player, skills, "Combat", 150);
        return;
      }
    }

    for (EntityType entityType : combatData.getLevel4Entities()) {
      if (inWorld2 || entity.getType() == entityType) {
        main.getExperienceManager().giveExperience(player, skills, "Combat", 250);
        return;
      }
    }

    for (EntityType entityType : combatData.getLevel5Entities()) {
      if (inWorld2 || entity.getType() == entityType) {
        main.getExperienceManager().giveExperience(player, skills, "Combat", 350);
        return;
      }
    }
  }

  @EventHandler
  public void onCreatureSpawn(CreatureSpawnEvent event) {

    Entity entity = event.getEntity();
    World world = entity.getLocation().getWorld();

    boolean netherEntity = Objects.requireNonNull(world).getName().endsWith("_nether");
    boolean theEndEntity = Objects.requireNonNull(world).getName().endsWith("_the_end");
    boolean spawnerEntity = event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER;

    if (spawnerEntity || netherEntity || theEndEntity) {
      event.getEntity().setMetadata("NO_XP", new FixedMetadataValue(main, event.getEntity()));
    }
  }

  @EventHandler
  public void onPlayerHit(EntityDamageByEntityEvent event) {

    Entity damager = event.getDamager();
    boolean inWorld2 = damager.getWorld().getName().contains("world2");

    if (!(damager instanceof Player) || inWorld2) {
      return;
    }

    Player player = (Player) damager;

    Material weapon = player.getInventory().getItemInMainHand().getType();

    String id = profileManager.getProfileId(player);
    Skills skills = profileManager.getPlayerProfile(id).getSkills();

    for (Material material : combatData.getIronWeapons()) {
      if (weapon == material) {
        int ironWeaponRequirement = 15;
        if (skills.getLevel("Combat") < ironWeaponRequirement) {
          player.sendMessage(
              color(
                  sdLabel
                      + " &4You need "
                      + ironWeaponRequirement
                      + " Combat to use Iron Weapons!"));
          event.setCancelled(true);
        }
      }
    }

    for (Material material : combatData.getGoldWeapons()) {
      if (weapon == material) {
        int goldWeaponRequirement = 20;
        if (skills.getLevel("Combat") < goldWeaponRequirement) {
          player.sendMessage(
              color(
                  sdLabel
                      + " &4You need "
                      + goldWeaponRequirement
                      + " Combat to use Gold Weapons!"));
          event.setCancelled(true);
        }
      }
    }

    for (Material material : combatData.getDiamondWeapons()) {
      if (weapon == material) {
        int diamondWeaponRequirement = 40;
        if (skills.getLevel("Combat") < diamondWeaponRequirement) {
          player.sendMessage(
              color(
                  sdLabel
                      + " &4You need "
                      + diamondWeaponRequirement
                      + " Combat to use Diamond Weapons!"));
          event.setCancelled(true);
        }
      }
    }

    for (Material material : combatData.getNetheriteWeapons()) {
      if (weapon == material) {
        int netheriteWeaponRequirement = 60;
        if (skills.getLevel("Combat") < netheriteWeaponRequirement) {
          player.sendMessage(
              color(
                  sdLabel
                      + " &4You need "
                      + netheriteWeaponRequirement
                      + " Combat to use Netherite Weapons!"));
          event.setCancelled(true);
        }
      }
    }
  }
}
