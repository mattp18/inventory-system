package com.code4joe.inventorysystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    private String getAllSQL = "SELECT * FROM categories";

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(getAllSQL);
             ResultSet resultSet = statement.executeQuery()) {

            while(resultSet.next()) {
                System.out.println("inside resultset of category service");
                categories.add(new Category(resultSet.getInt("id"),resultSet.getString("name")));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

//    public Category getCategoryById(int id) {
//
//    }
}
