package net.gigaclub.inventorysync.listener;

import net.gigaclub.inventorysync.data.InventoryData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OnJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(InventoryData.checkIfExists(player.getUniqueId().toString())) {
            Inventory inv = InventoryData.getInventory(player);
            int exp = InventoryData.getExperience(player);
            for(int i = 0; i < player.getInventory().getSize(); i++) {
                if(inv.getItem(i) != null) {
                    player.getInventory().setItem(i, inv.getItem(i));
                }
            }
            player.setTotalExperience(exp);
        } else  {
            InventoryData.setPlayerStats(player);
        }

    }

}
