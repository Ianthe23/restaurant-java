package controller;

import domain.MenuItem;
import domain.Order;
import enums.Status;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import service.RestaurantService;
import utils.events.RestaurantEvent;
import utils.observer.IObserver;

import java.util.List;
import java.util.stream.StreamSupport;

public class AngajatiController implements IObserver<RestaurantEvent> {
    RestaurantService service;

    Stage primaryStage;

    @FXML
    private ObservableList<Order> modelPlacedOrders = FXCollections.observableArrayList();

    @FXML
    private TableView<Order> placedOrdersTable;

    @FXML
    private TableColumn<Order, String> tableIdColumn;

    @FXML
    private TableColumn<Order, String> dateColumn;

    @FXML
    private TableColumn<Order, String> menuItemsColumn;

    public void setAngajatiController(RestaurantService service, Stage primaryStage) {
        this.service = service;
        this.primaryStage = primaryStage;

        service.addObserver(this);
        initPlacedOrders();
        initializeTable();
    }

    @FXML
    public void initialize() {
        initializeTable();
    }

    private void initializeTable() {
        tableIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTable().getId()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        menuItemsColumn.setCellValueFactory(cellData -> {
            // Generate a comma-separated string of menu items for this specific order
            String menuItems = cellData.getValue().getItems().stream()
                    .map(MenuItem::getItem) // Get the item name
                    .reduce((item1, item2) -> item1 + ", " + item2) // Concatenate with commas
                    .orElse(""); // Handle empty list
            return new SimpleStringProperty(menuItems);
        });
        placedOrdersTable.setItems(modelPlacedOrders);
    }

    private void initPlacedOrders() {
        modelPlacedOrders.clear();
        Iterable<Order> orders = service.getOrdersByStatus(Status.PLACED);
        List<Order> orderList = StreamSupport.stream(orders.spliterator(), false)
                .toList();
        modelPlacedOrders.setAll(orderList);
    }


    @Override
    public void update(RestaurantEvent restaurantEvent) {
        initPlacedOrders();
    }
}
