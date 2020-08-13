package me.stoladev.sdSkills.listeners;

import me.stoladev.sdSkills.Main;
import me.stoladev.sdSkills.managers.ProfileManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Objects;

import static me.stoladev.sdSkills.Utils.color;

public class InventoryClickListener implements Listener {

  private final ProfileManager profileManager;

  public InventoryClickListener(Main main) {
    main.getServer().getPluginManager().registerEvents(this, main);
    profileManager = main.getProfileManager();
  }

  @EventHandler
  public void onInvClick(InventoryClickEvent event) {
    Inventory inv = event.getInventory();
    Player player = (Player) event.getWhoClicked();

    // Checks if it's the Skill Tracker.
    if (!event
        .getView()
        .getTitle()
        .equalsIgnoreCase(color("&l" + player.getDisplayName() + "'s Skills"))) {
      return;
    }

    // Cancel all common functionality (taking items for example).
    event.setCancelled(true);

    // Prevent the player from using their own inventory.
    if (!Objects.requireNonNull(event.getClickedInventory()).equals(inv)) {
      return;
    }

    // Allow the player to use left click only on items (this doesn't include dragging).
    // This is here for future menu's detailing skills in-depth. DO NOT REMOVE.
    ClickType click = event.getClick();
    if (click != ClickType.LEFT) {}
  }
}
