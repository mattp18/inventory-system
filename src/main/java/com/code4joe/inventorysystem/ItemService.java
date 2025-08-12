package com.code4joe.inventorysystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ItemService {

    private String sql = "INSERT INTO items (name, date, sold, price, quantity, category_id) VALUES (?, ?, ?, ?, ?, ?)";

    private String eagerFetchItemsSQL = "SELECT i.id, i.name, i.date, i.sold, i.price, i.quantity, c.id AS category_id, c.name AS category_name\n" +
            "FROM items i\n" +
            "LEFT JOIN categories c ON i.category_id = c.id;\n";

    private String updateItemSQL = "UPDATE items SET name = ?, sold = ?, price = ?, date = ?, category_id = ? WHERE id = ?";

    private String removeItemSQL = "DELETE FROM items WHERE id = ?";

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
            statement.setBoolean(3, item.getSold());
            statement.setDouble(4, item.getPrice());
            statement.setInt(5, item.getQuantity());
            statement.setInt(6, item.getCategory().getId());

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
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        category
                );

                items.add(item);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    public void updateItem(int itemId, int categoryId, ItemDTO itemDTO ) {
        try(Connection connection = DatabaseConnector.getConnection();
            PreparedStatement statement = connection.prepareStatement(updateItemSQL);) {

            System.out.println("in Update item checking if id is present " + categoryId);

            statement.setString(1, itemDTO.getName());
            statement.setBoolean(2, itemDTO.getSold());
            statement.setDouble(3, itemDTO.getPrice());
            statement.setString(4, itemDTO.getDate());
            statement.setInt(5, categoryId);
            statement.setInt(6, itemId);


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

    public void removeItem(int itemId) {
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(removeItemSQL);) {
            statement.setInt(1, itemId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
