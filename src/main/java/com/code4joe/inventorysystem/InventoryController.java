package com.code4joe.inventorysystem;

import com.code4joe.inventorysystem.itemDetail.ItemDetailsController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class InventoryController {

    private final ItemService itemService = new ItemService();

    private final CategoryService categoryService = new CategoryService();

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
    private TableColumn<Item, String> priceColumn;

    @FXML
    private TableColumn<Item, String> dateColumn;

    @FXML
    private TableColumn<Item, String> categoryColumn;

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

    /**
     *  control and data structure variables.
     */

    private boolean recordClicked = false;

    private ObservableList<Item> itemList;


    @FXML
    public void initialize() {
        System.out.println("initialize");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        soldColumn.setCellValueFactory(new PropertyValueFactory<>("sold"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // category column - custom mapping
        categoryColumn.setCellValueFactory(cellData -> {
            Item item = cellData.getValue();
            if (item.getCategory() != null) {
                return new SimpleStringProperty(item.getCategory().getName());
            } else {
                return new SimpleStringProperty("No Category");
            }
        });

        itemList = FXCollections.observableArrayList();
        itemList.setAll(itemService.getAllItems());
        tableView.setItems(itemList);

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
                refreshTable();
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

    private void refreshTable() {
        System.out.println("refresh table");
        itemList.setAll(itemService.getAllItems());
    }

    private void showItemDetailsWindow(Item item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ItemDetails.fxml"));
            Parent root = loader.load();

            // Pass the selected item to the controller
            ItemDetailsController controller = loader.getController();
            controller.setItem(item);

            Stage stage = new Stage();
            stage.setTitle("Item Details");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}