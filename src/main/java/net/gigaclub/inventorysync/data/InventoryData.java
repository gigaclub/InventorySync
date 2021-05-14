package net.gigaclub.inventorysync.data;

import net.gigaclub.inventorysync.Main;
import net.gigaclub.inventorysync.helper.MySQLHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryData {

    public static void setUpTables() {
        MySQLHelper.update("CREATE TABLE IF NOT EXISTS PlayerInventory(UUID varchar(64), Inventory blob(16383), Experience BIGINT)");
    }

    public static void saveAll5Minutes() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                saveAllInvs();
            }
        }, 0, 20*60*5);
    }

    public static void saveAllInvs() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            setPlayerStats(player);
        }
    }

    public static void setPlayerStats(Player player) {
        if(checkIfExists(player.getUniqueId().toString())) {
            update(player);
        } else {
            create(player);
        }
    }

    public static Inventory getInventory(Player player) {
        try {
            PreparedStatement statement = MySQLHelper.connection.prepareStatement("SELECT * FROM PlayerInventory WHERE UUID=?;");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Inventory invData = decodeInventory(resultSet.getString("Inventory"));
            resultSet.close();
            statement.close();
            return invData;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static int getExperience(Player player) {
        try {
            PreparedStatement statement = MySQLHelper.connection.prepareStatement("SELECT * FROM PlayerInventory WHERE UUID=?;");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int experience = resultSet.getInt("Experience");
            resultSet.close();
            statement.close();
            return experience;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public static void update(Player player) {
        MySQLHelper.update("UPDATE PlayerInventory SET Inventory='" + encodeInventory(player.getInventory()) + "', Experience='" + player.getTotalExperience() + "' WHERE UUID='" + player.getUniqueId().toString() + "'");
    }

    public static void create(Player player) {
        try {
            PreparedStatement statement = MySQLHelper.connection.prepareStatement("INSERT INTO PlayerInventory values(?, ?, ?)");
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, encodeInventory(player.getInventory()));
            statement.setInt(3, player.getTotalExperience());
            statement.execute();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static boolean checkIfExists(String playerUUID) {
        try{
            PreparedStatement statement = MySQLHelper.connection.prepareStatement("SELECT * FROM PlayerInventory WHERE UUID=?");
            statement.setString(1, playerUUID);
            ResultSet resultSet = statement.executeQuery();
            boolean exists = resultSet.next();
            statement.close();
            resultSet.close();
            return exists;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String encodeInventory(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static Inventory decodeInventory(String data) {
        try {
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
                Inventory inventory = Bukkit.createInventory(null, dataInput.readInt() + 13);

                // Read the serialized inventory
                for (int i = 0; i < inventory.getSize() - 13; i++) {
                    inventory.setItem(i, (ItemStack) dataInput.readObject());
                }
                dataInput.close();
                return inventory;
            } catch (ClassNotFoundException e) {
                throw new IOException("Unable to decode class type.", e);
            }
        }catch (IOException e) {
        }
        return null;
    }

}
