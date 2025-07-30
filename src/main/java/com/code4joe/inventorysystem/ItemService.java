package com.code4joe.inventorysystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ItemService {

    private String sql = "INSERT INTO items (name, date, sold, price, category_id) VALUES (?, ?, ?, ?, ?)";

    String getAllSQL = "SELECT * FROM items";

    String eagerFetchItemsSQL = "SELECT i.id, i.name, i.date, i.sold, i.price, c.id AS category_id, c.name AS category_name\n" +
            "FROM items i\n" +
            "LEFT JOIN categories c ON i.category_id = c.id;\n";

    String selectWhereExistsSQL = "SELECT * FROM items WHERE id = ?";

    String updateItemSQL = "UPDATE items SET name = ?, sold = ?, price = ?, date = ? WHERE id = ?";

    /**
     * Add item to database.
     *
     * @param item item to be added to database.
     */
    public void addItem(Item item) {
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);) {

            statement.setString(1, item.getName());
            statement.setString(2, LocalDate.now().toString());
            statement.setBoolean(3, false);
            statement.setDouble(4, item.getPrice());
            statement.setInt(5, item.getCategory().getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(eagerFetchItemsSQL);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                // Build Category if it exists
                Category category = null;
                if (rs.getString("category_name") != null) {
                    category = new Category(
                            rs.getInt("category_id"),
                            rs.getString("category_name")
                    );
                }

                // Build Item including Category
                Item item = new Item(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("date"),
                        rs.getBoolean("sold"),
                        Double.valueOf(rs.getInt("price")),
                        category
                );

                items.add(item);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }


    public void updateItem(int id, ItemDTO itemDTO ) {
        try(Connection connection = DatabaseConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement(updateItemSQL);) {

            System.out.println("in Update item checking if id is present " + id);

            statement.setString(1, itemDTO.getName());
            statement.setBoolean(2, itemDTO.getSold());
            statement.setDouble(3, itemDTO.getPrice());
            statement.setString(4, itemDTO.getDate());
            statement.setInt(5, id);


            boolean statementExecutedSuccessfully = statement.execute();

            if(statementExecutedSuccessfully) {
                System.out.println("Item updated successfully");
            } else {
                System.out.println("Item could not be updated");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Delete item method

    //Add item method

    //update item method

}
