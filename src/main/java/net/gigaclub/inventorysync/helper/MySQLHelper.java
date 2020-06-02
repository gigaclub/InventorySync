package net.gigaclub.inventorysync.helper;

import net.gigaclub.inventorysync.config.MySQLConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLHelper {

    public static Connection connection;

    public static void connect() {
        try{
            connection = DriverManager.getConnection("jdbc:mysql://" + MySQLConfig.getMySQLString("Host") + ":" + MySQLConfig.getMySQLString("Port") + "/" + MySQLConfig.getMySQLString("Datenbank") + "?autoReconnect=true", MySQLConfig.getMySQLString("User"), MySQLConfig.getMySQLString("Passwort"));
            System.out.println("[MySQL] Die Verbindung zur MySQL wurde hergestellt!");
        } catch(SQLException e ) {
            System.out.println("[MySQL] Die Verbindung zur MySQL ist fehlgeschlagen! Fehler: " + e.getMessage());
        }
    }

    public static void disconnect() {
        if(connection != null) {
            try {
                connection.close();
                System.out.println("[MySQL] Die Verbindung zur MySQL wurde Erfolgreich beendet!");
            } catch (SQLException e) {
                System.out.println("[MySQL] Fehler beim beenden der Verbindung zur MySQL! Fehler: " + e.getMessage());
            }
        }
    }

    public static void update(String qry) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(qry);
            stmt.close();
        } catch (SQLException ex) {

            ex.printStackTrace();
            disconnect();
            connect();
        }
    }

}
