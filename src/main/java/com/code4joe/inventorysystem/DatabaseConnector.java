package com.code4joe.inventorysystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Matthew Puentes
 */

public class DatabaseConnector {

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // 1. Set up local DB location (user's home folder)
                String dbFolderPath = System.getProperty("user.home") + "/inventoryDB";
                Path dbFolder = Paths.get(dbFolderPath);
                Files.createDirectories(dbFolder);

                // 2. Full path to database file
                String dbFilePath = dbFolderPath + "/inventory.db";

                // 3. Create the connection
                String url = "jdbc:sqlite:" + dbFilePath;
                connection = DriverManager.getConnection(url);

            } catch (IOException e) {
                throw new SQLException("Failed to create database directory", e);
            }
        }
        return connection;
    }
}
