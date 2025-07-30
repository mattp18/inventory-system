package com.code4joe.inventorysystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    static Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

    public static void initialize() {

        log.info("Attempting to initialize DB ...");

        try(Connection connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();) {

            statement.execute("PRAGMA foreign_keys=ON");

            statement.execute("""
    CREATE TABLE IF NOT EXISTS categories (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name VARCHAR(255)
    )
""");

            statement.execute("""
    CREATE TABLE IF NOT EXISTS items (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name VARCHAR(255),
        image VARCHAR(255),
        date DATE,
        price DOUBLE,
        category_id INTEGER,
        sold BOOLEAN DEFAULT FALSE,
        
        FOREIGN KEY (category_id) REFERENCES categories(id)
    )
""");

            log.info("Successfully initialized DB");



        }catch( SQLException exception) {
            //do something if exception is thrown
            exception.printStackTrace();

        }

    }
}
