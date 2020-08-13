package me.stoladev.sdSkills.listeners;

import me.stoladev.sdSkills.Main;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

import static me.stoladev.sdSkills.Utils.log;

public class GrowthListener implements Listener {

  private final Main main;

  public GrowthListener(Main main) {
    main.getServer().getPluginManager().registerEvents(this, main);
    this.main = main;
  }

  @EventHandler
  private void onCropGrowth(BlockGrowEvent event) {

    Block block = event.getBlock();

    if (block.hasMetadata("PLACED")) {
      block.removeMetadata("PLACED", main);
      log(block.getType() + " grew. Removed Metadata.");
    }
  }
}
