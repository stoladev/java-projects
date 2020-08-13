package me.stoladev.sdSkills.listeners;

import me.stoladev.sdSkills.Main;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class BlockPlaceListener implements Listener {

  private final Main main;

  public BlockPlaceListener(Main main) {
    main.getServer().getPluginManager().registerEvents(this, main);
    this.main = main;
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event) {

    Block block = event.getBlock();

    if (!block.hasMetadata("PLACED")) {
      block.setMetadata("PLACED", new FixedMetadataValue(main, event.getPlayer()));
    }
  }
}
