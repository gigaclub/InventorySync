package net.gigaclub.inventorysync.listener;

import net.gigaclub.inventorysync.data.InventoryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnLeave implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        InventoryData.setPlayerStats(event.getPlayer());
    }
}
