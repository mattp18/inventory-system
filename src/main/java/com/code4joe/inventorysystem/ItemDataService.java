package com.code4joe.inventorysystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Matthew Puentes
 */

public class ItemDataService {

    private final ItemService itemService = new ItemService();

    private final ObservableList<Item> items = FXCollections.observableArrayList();

    public ObservableList<Item> getItems() {
        return items;
    }

    public void refreshItems() {
        System.out.println("Refreshing Items");
        items.setAll(itemService.getAllItems());
    }
}
