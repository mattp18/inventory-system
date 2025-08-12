package com.code4joe.inventorysystem.addItem;

import com.code4joe.inventorysystem.Category;
import com.code4joe.inventorysystem.CategoryService;
import com.code4joe.inventorysystem.Item;
import com.code4joe.inventorysystem.ItemService;
import com.code4joe.inventorysystem.itemDetail.ItemDetailsController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;


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

    @FXML
    private CheckBox checkBox;

    public void initialize() {
        categoryComboBox.setItems(FXCollections.observableArrayList(categoryService.getAllCategories()));

        if(!categoryComboBox.getItems().isEmpty()) {
            categoryComboBox.setValue(categoryComboBox.getItems().getFirst());
        }
    }


    @FXML
    public void addItemHandleSubmit() {
        System.out.println("addItemHandleSubmit testing");
        //retrieve category object by name

        if (ItemDetailsController.priceValidation(itemPriceField)) return;

        if(!itemNameField.getText().trim().isEmpty() && !itemPriceField.getText().trim().isEmpty() && !categoryComboBox.getSelectionModel().isEmpty()) {
            Item item = new Item(itemNameField.getText().trim(), Double.parseDouble(itemPriceField.getText().trim()), categoryComboBox.getValue());
            item.setSold(checkBox.isSelected());
            itemService.addItem(item);
            System.out.println("persisting item to db...");

            //clear input fields
            itemNameField.clear();
            itemPriceField.clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Missing values");
            alert.setContentText("Name and Price are required");

            alert.showAndWait();
        }
    }
}