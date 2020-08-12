package me.stoladev.sdSkills.managers;

import me.stoladev.sdSkills.Main;
import me.stoladev.sdSkills.commands.ChatLevelCommand;
import me.stoladev.sdSkills.commands.GiveExperienceCommand;
import me.stoladev.sdSkills.commands.SetLevelCommand;
import me.stoladev.sdSkills.commands.SkillCommand;

import java.util.Objects;

public class CommandManager {

  private final Main main;

  public CommandManager(Main main) {
    this.main = main;
  }

  public void loadCommands() {
    defaultUserCommands();
    adminUserCommands();
  }

  private void defaultUserCommands() {
    interfaceCommands();
    chatLevelCommands();
  }

  private void adminUserCommands() {
    giveExperienceCommands();
    setLevelCommands();
  }

  private void interfaceCommands() {
    SkillCommand skillCommand = new SkillCommand(main);
    Objects.requireNonNull(main.getCommand("skills")).setExecutor(skillCommand);
    Objects.requireNonNull(main.getCommand("skills")).setTabCompleter(skillCommand);
  }

  private void chatLevelCommands() {
    ChatLevelCommand chatLevelCommand = new ChatLevelCommand(main);
    Objects.requireNonNull(main.getCommand("chatcombat")).setExecutor(chatLevelCommand);
    Objects.requireNonNull(main.getCommand("chatcombat")).setTabCompleter(chatLevelCommand);
    Objects.requireNonNull(main.getCommand("chatmining")).setExecutor(chatLevelCommand);
    Objects.requireNonNull(main.getCommand("chatmining")).setTabCompleter(chatLevelCommand);
    Objects.requireNonNull(main.getCommand("chatfarming")).setExecutor(chatLevelCommand);
    Objects.requireNonNull(main.getCommand("chatfarming")).setTabCompleter(chatLevelCommand);
    Objects.requireNonNull(main.getCommand("chatagility")).setExecutor(chatLevelCommand);
    Objects.requireNonNull(main.getCommand("chatagility")).setTabCompleter(chatLevelCommand);
    Objects.requireNonNull(main.getCommand("chattotal")).setExecutor(chatLevelCommand);
    Objects.requireNonNull(main.getCommand("chattotal")).setTabCompleter(chatLevelCommand);
  }

  private void giveExperienceCommands() {
    GiveExperienceCommand giveExperienceCommand = new GiveExperienceCommand(main);
    Objects.requireNonNull(main.getCommand("givecombat")).setExecutor(giveExperienceCommand);
    Objects.requireNonNull(main.getCommand("givecombat")).setTabCompleter(giveExperienceCommand);
    Objects.requireNonNull(main.getCommand("givemining")).setExecutor(giveExperienceCommand);
    Objects.requireNonNull(main.getCommand("givemining")).setTabCompleter(giveExperienceCommand);
    Objects.requireNonNull(main.getCommand("givefarming")).setExecutor(giveExperienceCommand);
    Objects.requireNonNull(main.getCommand("givefarming")).setTabCompleter(giveExperienceCommand);
    Objects.requireNonNull(main.getCommand("giveagility")).setExecutor(giveExperienceCommand);
    Objects.requireNonNull(main.getCommand("giveagility")).setTabCompleter(giveExperienceCommand);
  }

  private void setLevelCommands() {
    SetLevelCommand setLevelCommand = new SetLevelCommand(main);
    Objects.requireNonNull(main.getCommand("setcombat")).setExecutor(setLevelCommand);
    Objects.requireNonNull(main.getCommand("setcombat")).setTabCompleter(setLevelCommand);
    Objects.requireNonNull(main.getCommand("setmining")).setExecutor(setLevelCommand);
    Objects.requireNonNull(main.getCommand("setmining")).setTabCompleter(setLevelCommand);
    Objects.requireNonNull(main.getCommand("setfarming")).setExecutor(setLevelCommand);
    Objects.requireNonNull(main.getCommand("setfarming")).setTabCompleter(setLevelCommand);
    Objects.requireNonNull(main.getCommand("setagility")).setExecutor(setLevelCommand);
    Objects.requireNonNull(main.getCommand("setagility")).setTabCompleter(setLevelCommand);
  }
}
