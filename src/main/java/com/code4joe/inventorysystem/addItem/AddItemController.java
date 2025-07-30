package com.code4joe.inventorysystem.addItem;

import com.code4joe.inventorysystem.Category;
import com.code4joe.inventorysystem.CategoryService;
import com.code4joe.inventorysystem.Item;
import com.code4joe.inventorysystem.ItemService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.List;

public class AddItemController {

    private final ItemService itemService = new ItemService();

    private final CategoryService categoryService = new CategoryService();

    @FXML
    private Button submitAddButton;

    @FXML
    private TextField itemNameField;

    @FXML
    private TextField itemPriceField;

    @FXML
    private ComboBox<Category> categoryComboBox;

    public void initialize() {
        categoryComboBox.setItems(FXCollections.observableArrayList(categoryService.getAllCategories()));
    }


    @FXML
    public void addItemHandleSubmit() {
        System.out.println("addItemHandleSubmit testing");
        //retrieve category object by name


        if(!itemNameField.getText().trim().isEmpty() && !itemPriceField.getText().trim().isEmpty() && !categoryComboBox.getSelectionModel().isEmpty()) {
            Item item = new Item(itemNameField.getText().trim(), Double.parseDouble(itemPriceField.getText().trim()), categoryComboBox.getValue());
            itemService.addItem(item);
            System.out.println("persisting item to db...");
        } else {
            System.out.println("Both fields cannot be empty");
        }
    }

    private void populateCategoryComboBox(List<Category> categories) {
        if(categories != null) {
            categoryComboBox.getItems().addAll(categories);
        }
    }
}
