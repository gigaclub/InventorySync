package net.gigaclub.inventorysync.config;

import net.gigaclub.inventorysync.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MySQLConfig {

    public static File fileMysql = new File("plugins//" + Main.getInstance().getDescription().getName(),"mysql.yml");
    public static YamlConfiguration mysql = YamlConfiguration.loadConfiguration(fileMysql);


    public static void setConfigs() {

        File ord = new File("plugins/" + Main.getInstance().getDescription().getName());
        if(!ord.exists()) {

            ord.mkdir();
        }

        File Mysql = new File("plugins/" + Main.getInstance().getDescription().getName() + "/mysql.yml");
        if(!Mysql.exists()) {
            try{

                Mysql.createNewFile();
            } catch(IOException ex) {

                ex.printStackTrace();
            }
        }

        mysql.addDefault("InventorySync.MySQL.Host", "localhost");
        mysql.addDefault("InventorySync.MySQL.Port", "3306");
        mysql.addDefault("InventorySync.MySQL.User", "user");
        mysql.addDefault("InventorySync.MySQL.Datenbank", "datenbank");
        mysql.addDefault("InventorySync.MySQL.Passwort", "passwort");

        try{
            mysql.options().copyDefaults(true);
            mysql.save(fileMysql);
        } catch(IOException ex) {

            ex.printStackTrace();
        }

    }


    public static String getMySQLString(String string) {
        String message = "";

        if(fileMysql.exists()) {
            if(mysql.getString("InventorySync.MySQL.Host") != null) {

                message = mysql.getString("InventorySync.MySQL." + string);

            }
        }
        return message;
    }


}
