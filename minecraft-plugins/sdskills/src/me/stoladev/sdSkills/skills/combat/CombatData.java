package me.stoladev.sdSkills.skills.combat;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class CombatData {

  private final EntityType[] Level1Entities =
      new EntityType[] {EntityType.ZOMBIE, EntityType.HUSK, EntityType.SKELETON, EntityType.SPIDER};

  private final EntityType[] Level2Entities =
      new EntityType[] {
        EntityType.SILVERFISH,
        EntityType.ENDERMITE,
        EntityType.ZOMBIE_VILLAGER,
        EntityType.SLIME,
        EntityType.MAGMA_CUBE,
        EntityType.PILLAGER,
        EntityType.WITCH
      };

  private final EntityType[] Level3Entities =
      new EntityType[] {
        EntityType.CAVE_SPIDER,
        EntityType.VINDICATOR,
        EntityType.RAVAGER,
        EntityType.WITHER_SKELETON,
        EntityType.BLAZE,
        EntityType.STRAY,
        EntityType.HOGLIN,
        EntityType.PIGLIN
      };

  private final EntityType[] Level4Entities =
      new EntityType[] {
        EntityType.CREEPER,
        EntityType.DROWNED,
        EntityType.PHANTOM,
        EntityType.ENDERMAN,
        EntityType.ZOMBIFIED_PIGLIN,
        EntityType.GUARDIAN,
        EntityType.ELDER_GUARDIAN,
        EntityType.GHAST,
        EntityType.SHULKER,
        EntityType.SKELETON_HORSE
      };

  private final EntityType[] Level5Entities =
      new EntityType[] {
        EntityType.WITHER, EntityType.VEX, EntityType.EVOKER, EntityType.ILLUSIONER
      };

  private final Material[] IronWeapons =
      new Material[] {
        Material.IRON_SWORD,
        Material.IRON_AXE,
        Material.IRON_HOE,
        Material.IRON_PICKAXE,
        Material.IRON_SHOVEL
      };

  private final Material[] GoldWeapons =
      new Material[] {
        Material.GOLDEN_SWORD,
        Material.GOLDEN_AXE,
        Material.GOLDEN_HOE,
        Material.GOLDEN_PICKAXE,
        Material.GOLDEN_SHOVEL
      };

  private final Material[] DiamondWeapons =
      new Material[] {
        Material.DIAMOND_SWORD,
        Material.DIAMOND_AXE,
        Material.DIAMOND_HOE,
        Material.DIAMOND_PICKAXE,
        Material.DIAMOND_SHOVEL
      };

  Material[] NetheriteWeapons =
      new Material[] {
        Material.NETHERITE_SWORD,
        Material.NETHERITE_AXE,
        Material.NETHERITE_HOE,
        Material.NETHERITE_PICKAXE,
        Material.NETHERITE_SHOVEL
      };

  public EntityType[] getLevel1Entities() {
    return Level1Entities;
  }

  public EntityType[] getLevel2Entities() {
    return Level2Entities;
  }

  public EntityType[] getLevel3Entities() {
    return Level3Entities;
  }

  public EntityType[] getLevel4Entities() {
    return Level4Entities;
  }

  public EntityType[] getLevel5Entities() {
    return Level5Entities;
  }

  public Material[] getIronWeapons() {
    return IronWeapons;
  }

  public Material[] getGoldWeapons() {
    return GoldWeapons;
  }

  public Material[] getDiamondWeapons() {
    return DiamondWeapons;
  }

  public Material[] getNetheriteWeapons() {
    return NetheriteWeapons;
  }
}
