package me.stoladev.sdSkills.components;

import java.util.Arrays;
import java.util.List;

public class Skills {

  private final int[] xpLevels =
      new int[] {
        0, 83, 174, 276, 388, 512, 650, 801, 969, 1154, 1358, 1584, 1833, 2107, 2411, 2746, 3115,
        3523, 3973, 4470, 5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031, 13363, 14833,
        16456, 18247, 20224, 22406, 24816, 27473, 30408, 33648, 37224, 41171, 45529, 50339, 55649,
        61512, 67983, 75144, 83014, 91721, 101333, 111945, 123660, 136594, 150872, 166636, 184040,
        203254, 224466, 247886, 273742, 302288, 333804, 368599, 407015, 449428, 496254, 547953,
        605032, 668051, 737637, 814445, 899257, 992895, 1096278, 1210421, 1336443, 1475581, 1629200,
        1798808, 1986068, 2192818, 2421087, 2673114, 2951373, 3258594, 3597792, 3972294, 4385776,
        4842295, 5346332, 5902831, 6517253, 7195629, 7944614, 8771558, 9684577, 10692629, 11805606,
        13034431
      };
  private final List<Integer> broadcastLevels =
      Arrays.asList(
          15, 30, 45, 60, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92,
          93, 94, 95, 96, 97, 98, 99);

  private final int totalExperience;
  private int combatExperience;
  private int miningExperience;
  private int woodcuttingExperience;
  private int farmingExperience;
  private int agilityExperience;

  public Skills(
      int totalExperience,
      int combatExperience,
      int miningExperience,
      int woodcuttingExperience,
      int farmingExperience,
      int agilityExperience) {
    this.totalExperience = totalExperience;
    this.combatExperience = combatExperience;
    this.miningExperience = miningExperience;
    this.woodcuttingExperience = woodcuttingExperience;
    this.farmingExperience = farmingExperience;
    this.agilityExperience = agilityExperience;
  }

  public List<Integer> getBroadcastLevels() {
    return broadcastLevels;
  }

  public int getLevel(String skillName) {
    return calculateLevel(getXP(skillName));
  }

  public int getTotalLevel() {
    int total = 0;

    total += getLevel("Combat");
    total += getLevel("Mining");
    total += getLevel("Woodcutting");
    total += getLevel("Farming");
    total += getLevel("Agility");

    return total;
  }

  public int getXP(String skillName) {

    int experience;

    switch (skillName) {
      case "Total":
        experience =
            combatExperience
                + miningExperience
                + woodcuttingExperience
                + farmingExperience
                + agilityExperience;
        break;
      case "Combat":
        experience = combatExperience;
        break;
      case "Mining":
        experience = miningExperience;
        break;
      case "Woodcutting":
        experience = woodcuttingExperience;
        break;
      case "Farming":
        experience = farmingExperience;
        break;
      case "Agility":
        experience = agilityExperience;
        break;
      default:
        return 0;
    }
    return experience;
  }

  public void setLevel(Skills skills, String skillName, int level) {
    skills.setXP(skillName, skills.calculateXP(level - 1));
  }

  public void setXP(String skillName, int experience) {

    switch (skillName) {
      case "Combat":
        this.combatExperience = experience;
        break;
      case "Mining":
        this.miningExperience = experience;
        break;
      case "Woodcutting":
        this.woodcuttingExperience = experience;
        break;
      case "Farming":
        this.farmingExperience = experience;
        break;
      case "Agility":
        this.agilityExperience = experience;
        break;
    }
  }

  private int calculateLevel(int experience) {

    for (int level = 1; level < xpLevels.length; level++) {
      if (xpLevels[level] > experience) {
        return level;
      }
    }
    return 99;
  }

  public int calculateXP(int level) {
    return xpLevels[level];
  }

  public int calculateXPToGoal(int currentXP, int goalLevel) {
    return xpLevels[goalLevel - 1] - currentXP;
  }
}
