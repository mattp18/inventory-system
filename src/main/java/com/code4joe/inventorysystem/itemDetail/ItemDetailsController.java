package com.code4joe.inventorysystem.itemDetail;

import com.code4joe.inventorysystem.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Optional;

public class ItemDetailsController {

    private final ItemService itemService = new ItemService();

    private final CategoryService categoryService = new CategoryService();

    private ItemDataService itemDataService;

    private int itemId;

    @FXML
    private Label nameLabel;

    @FXML private Label priceLabel;

    @FXML private TextField nameField;

    @FXML
    private CheckBox checkBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField priceField;

    @FXML
    private Button submitButton;

    @FXML
    private ComboBox<Category> categoryComboBox;

    @FXML
    private ImageView iconDeleteItem;

    public void setItemDataService(ItemDataService itemDataService) {
        this.itemDataService = itemDataService;
    }

    public void initialize() {
        categoryComboBox.setItems(FXCollections.observableArrayList(categoryService.getAllCategories()));
    }

    public void setItem(Item item) {
        itemId = item.getId();
        nameLabel.setText("Name: ");
        priceLabel.setText("Price: $");
        nameField.setText(item.getName());
        priceField.setText(String.format("%.2f", item.getPrice()));
        checkBox.setSelected(item.getSold());
        LocalDate localDate = LocalDate.parse(item.getDate());
        datePicker.setValue(localDate);

        if(item.getCategory() != null) {
            categoryComboBox.setValue(item.getCategory());
        }
    }

    public void submitUpdateItem() {
        ItemDTO itemDTO = null;
        if(datePicker.getValue() != null) {
            itemDTO = new ItemDTO(nameField.getText(), Double.parseDouble(priceField.getText()), datePicker.getValue().toString(), checkBox.isSelected());
        } else {
            itemDTO = new ItemDTO(nameField.getText(), Double.parseDouble(priceField.getText()), checkBox.isSelected());
        }
            itemService.updateItem(itemId, categoryComboBox.getValue().getId(), itemDTO);

        itemDataService.refreshItems();

        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleIconClicked(MouseEvent event) {
        System.out.println("test");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Item");
        alert.setHeaderText("Are you sure you want to delete this item?");
        alert.setContentText("This action cannot be undone");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            itemService.removeItem(itemId);
            itemDataService.refreshItems();
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();
        } else {
            System.out.println("User canceled");
        }
    }
}
