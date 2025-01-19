package controller;

import domain.MenuItem;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import service.RestaurantService;
import javafx.beans.property.SimpleStringProperty;

import java.util.HashMap;
import java.util.Map;

public class MenuController {
    RestaurantService service;

    @FXML
    private Label menu_id;

    @FXML
    private VBox tableContainer;

    @FXML
    private Button place_order_btn;

    private Map<TableView<MenuItem>, ObservableList<MenuItem>> selectedItemsMap = new HashMap<>();

    @FXML
    public void initialize() {
    }

    @FXML
    private void handlePlacingOrder() {
        System.out.println("Placing order");
        // Get the selected items from all tables
        ObservableList<MenuItem> selectedItems = FXCollections.observableArrayList();
        selectedItemsMap.values().forEach(selectedItems::addAll);

        service.addOrder(menu_id.getText(), selectedItems);
    }

    private void initializeVBox() {
        for (String category : service.getCategoriesFromMenu()) {
            Label categoryLabel = new Label(category);

            TableView<MenuItem> tableView = new TableView<>();
            tableView.setPrefHeight(200);
            tableView.setPrefWidth(300);

            tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            TableColumn<MenuItem, String> column1 = new TableColumn<>("Item Name");
            TableColumn<MenuItem, String> column2 = new TableColumn<>("Price");

            column1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem()));
            column2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrice() + " " + cellData.getValue().getCurrency()));

            tableView.getColumns().add(column1);
            tableView.getColumns().add(column2);

            tableView.getItems().addAll(service.getMenuItemsByCategory(category));
            // Listen for changes to the selected items using ListChangeListener
            tableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<MenuItem>) change -> {
                // Track the selected items for this table
                updateSelectedItems(tableView, tableView.getSelectionModel().getSelectedItems());
            });


            tableContainer.getChildren().addAll(categoryLabel, tableView);
        }
    }

    private void updateSelectedItems(TableView<MenuItem> tableView, ObservableList<MenuItem> selectedItems) {
        selectedItemsMap.put(tableView, selectedItems);

        // Optionally, log or handle the selections
        System.out.println("Selected items from table: " + tableView);
        for (MenuItem item : selectedItems) {
            System.out.println(item.getItem());
        }
    }

    public void setLabelText(String text) {
        menu_id.setText(text);
    }

    public void setService(RestaurantService service) {
        this.service = service;

        initializeVBox();
    }
}
