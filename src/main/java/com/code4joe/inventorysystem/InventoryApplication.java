package com.code4joe.inventorysystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InventoryApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        ItemDataService itemDataService = new ItemDataService();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/code4joe/inventorysystem/home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        // give InventoryController the shared service
        InventoryController inventoryController = fxmlLoader.getController();
        inventoryController.setItemDataService(itemDataService);

        stage.setTitle("Snagged Inventory System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        DatabaseInitializer.initialize();
        launch();
    }
}