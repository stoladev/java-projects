package me.stoladev.sdSkills.listeners;

import me.stoladev.sdSkills.Main;
import me.stoladev.sdSkills.skills.farming.Farming;
import me.stoladev.sdSkills.skills.mining.Mining;
import me.stoladev.sdSkills.skills.woodcutting.Woodcutting;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import static me.stoladev.sdSkills.Utils.*;

public class BlockBreakListener implements Listener {

  private final Mining miningListener;
  private final Farming farming;
  private final Woodcutting woodcutting;

  public BlockBreakListener(Main main) {
    main.getServer().getPluginManager().registerEvents(this, main);

    this.miningListener = new Mining(main);
    log(color(sdLabelAdmin + " Enabled Mining."));

    this.farming = new Farming(main);
    log(color(sdLabelAdmin + " Enabled Farming."));

    this.woodcutting = new Woodcutting(main);
    log(color(sdLabelAdmin + " Enabled Woodcutting."));
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {

    Block block = event.getBlock();

    // Check if block was placed before.
    if (block.hasMetadata("PLACED")) {
      return;
    }

    // Checks if the broken block was an ore.
    boolean minedAnOre = miningListener.onMine(event);
    if (minedAnOre) return;

    // Checks if the broken block was wood.
    boolean choppedWood = woodcutting.onChop(event);
    if (choppedWood) return;

    // Checks if the broken block was farming related.
    boolean farmedACrop = farming.onFarm(event);
    farming.onFarm(event);
  }
}
