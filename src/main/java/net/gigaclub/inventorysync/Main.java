package net.gigaclub.inventorysync;

import net.gigaclub.inventorysync.config.MySQLConfig;
import net.gigaclub.inventorysync.data.InventoryData;
import net.gigaclub.inventorysync.helper.MySQLHelper;
import net.gigaclub.inventorysync.listener.OnJoin;
import net.gigaclub.inventorysync.listener.OnLeave;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;

public final class Main extends JavaPlugin {

    public static Main instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        setInstance(this);

        MySQLConfig.setConfigs();
        registerListener();

        MySQLHelper.connect();
        InventoryData.setUpTables();

        InventoryData.saveAll5Minutes();


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        InventoryData.saveAllInvs();

        MySQLHelper.disconnect();

    }

    public void setInstance(Main instance) {
        Main.instance = instance;
    }

    public static Main getInstance() {
        return instance;
    }

    public void registerListener() {
        getServer().getPluginManager().registerEvents(new OnJoin(), this);
        getServer().getPluginManager().registerEvents(new OnLeave(), this);
    }

}
