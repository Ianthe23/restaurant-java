package controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import service.RestaurantService;

public class AngajatiController {
    RestaurantService service;

    Stage primaryStage;

    public void setAngajatiController(RestaurantService service, Stage primaryStage) {
        this.service = service;
        this.primaryStage = primaryStage;
    }
}
