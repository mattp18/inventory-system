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
        quantity INTEGER DEFAULT 0,
        
        FOREIGN KEY (category_id) REFERENCES categories(id)
    )
""");

            // Populate default categories if none exist
            populateDefaultCategories(connection);

            log.info("Successfully initialized DB");



        }catch( SQLException exception) {
            //do something if exception is thrown
            exception.printStackTrace();

        }

    }

    private static void populateDefaultCategories(Connection connection) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM categories";
        try (Statement stmt = connection.createStatement();
             var rs = stmt.executeQuery(checkSql)) {

            if (rs.next() && rs.getInt(1) == 0) {
                log.info("No categories found — inserting default categories...");
                try (var ps = connection.prepareStatement(
                        "INSERT INTO categories (name) VALUES (?), (?), (?), (?), (?), (?), (?), (?), (?), (?), (?), (?), (?), (?)")) {
                    ps.setString(1, "Electronics");
                    ps.setString(2, "Collectibles & Art");
                    ps.setString(3, "Home & Garden");
                    ps.setString(4, "Clothing, Shoes, & Accessories");
                    ps.setString(5, "Toys & Hobbies");
                    ps.setString(6, "Sporting Goods");
                    ps.setString(7, "Books, Movies & Music");
                    ps.setString(8, "Health & Beauty");
                    ps.setString(9, "Business & Industrial");
                    ps.setString(10, "Jewelry & Watches");
                    ps.setString(11, "Baby Essentials");
                    ps.setString(12, "Pet Supplies");
                    ps.setString(13, "Tickets & Travel");
                    ps.setString(14, "Other");
                    ps.executeUpdate();
                }
            } else {
                log.info("Categories already populated, skipping...");
            }
        }
    }
}
