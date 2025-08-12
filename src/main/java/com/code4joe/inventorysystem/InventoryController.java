package com.code4joe.inventorysystem;

import com.code4joe.inventorysystem.itemDetail.ItemDetailsController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Matthew Puentes
 */

public class InventoryController {

    private ItemDataService itemDataService;

    /**
     * All table items
     */
    @FXML
    private TableView<Item> tableView;

    @FXML
    private TableColumn<Item, String> nameColumn;

    @FXML
    private TableColumn<Item, Boolean> soldColumn;

    @FXML
    private TableColumn<Item, Double> priceColumn;

    @FXML
    private TableColumn<Item, String> dateColumn;

    @FXML
    private TableColumn<Item, String> categoryColumn;

    @FXML
    private TableColumn<Item, Integer> quantityColumn;

    /**
     * All tab items
     */
    @FXML
    private TabPane tabPane;

    @FXML
    private Tab homeTab;

    @FXML
    private Tab addItemTab;

    @FXML private PieChart pieChart;

    @FXML private LineChart<Number, Number> lineChart;

    @FXML
    private TextField filterTextField;

    private FilteredList<Item> filteredItems;
    private SortedList<Item> sortedItems;

    /**
     *  control and data structure variables.
     */

    public void setItemDataService(ItemDataService itemDataService) {
        this.itemDataService = itemDataService;
        postInit();
    }

    @FXML
    public void initialize() {
        System.out.println("initialize");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        soldColumn.setCellValueFactory(new PropertyValueFactory<>("sold"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellFactory(tc -> new TableCell<Item, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", price));
                }
            }
        });


        // category column - custom mapping
        categoryColumn.setCellValueFactory(cellData -> {
            Item item = cellData.getValue();
            if (item.getCategory() != null) {
                return new SimpleStringProperty(item.getCategory().getName());
            } else {
                return new SimpleStringProperty("No Category");
            }
        });

        /**
         * Pie Chart Impl.
         */
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
                new PieChart.Data("Trading Cards", 40),
                new PieChart.Data("Vintage", 20),
                new PieChart.Data("Sports", 30),
                new PieChart.Data("Marvel", 10)
        );

        pieChart.setData(data);
        pieChart.setTitle("Sold Items By Category");


        /**
         * Line Chart Impl.
         */
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Items Sold");

        // Sample data points: (day, items sold)
        series.getData().add(new XYChart.Data<>(1, 20));
        series.getData().add(new XYChart.Data<>(2, 30));
        series.getData().add(new XYChart.Data<>(3, 15));
        series.getData().add(new XYChart.Data<>(4, 40));
        series.getData().add(new XYChart.Data<>(5, 25));

        lineChart.getData().add(series);


        /**
         * Tab pane listener.
         */
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newTab) -> {

            if(newTab == addItemTab ) {
                tableView.setVisible(false);
                try {
                    Node content = FXMLLoader.load(getClass().getResource("/com/code4joe/inventorysystem/addItem.fxml"));
                    addItemTab.setContent(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if(newTab == homeTab ) {
                itemDataService.refreshItems();
                tableView.setVisible(true);
            }
        });

        tableView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2 ) {
                Item selectedItem = tableView.getSelectionModel().getSelectedItem();
                System.out.println(selectedItem.getId());
                showItemDetailsWindow(selectedItem);
            }
        });
    }

    private void postInit() {
        // 1) wrap the service list in a FilteredList (same backing list)
        filteredItems = new FilteredList<>(itemDataService.getItems(), p -> true);

        // 2) wrap in a SortedList so TableView sorting works
        sortedItems = new SortedList<>(filteredItems);
        sortedItems.comparatorProperty().bind(tableView.comparatorProperty());

        // 3) set the table once
        tableView.setItems(sortedItems);

        // 4) set up listener (do this after filteredItems exists)
        filterTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            final String query = (newValue == null) ? "" : newValue.trim().toLowerCase();

            filteredItems.setPredicate(item -> {
                if (query.isEmpty()) return true;

                // protect against nulls
                String name = item.getName() == null ? "" : item.getName().toLowerCase();
                if (name.contains(query)) return true;

                Category cat = item.getCategory();
                if (cat != null) {
                    String catName = cat.getName() == null ? "" : cat.getName().toLowerCase();
                    if (catName.contains(query)) return true;
                }

                return false;
            });
        });

        // 5) load data into the service (this updates filteredItems automatically)
        itemDataService.refreshItems();
    }

    private void showItemDetailsWindow(Item item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ItemDetails.fxml"));
            Parent root = loader.load();

            // Pass the selected item to the controller
            ItemDetailsController detailsController = loader.getController();
            detailsController.setItemDataService(itemDataService);
            detailsController.setItem(item);

            Stage stage = new Stage();
            stage.setTitle("Item Details");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}