package org.example.restaurante;

import controller.AngajatiController;
import domain.*;
import domain.validator.EValidator;
import domain.validator.IValidator;
import domain.validator.ValidatorFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repo.database.factory.DataBaseRepoFactory;
import repo.database.factory.EDataBaseStrategy;
import repo.database.utils.AbstractDataBaseRepo;
import repo.database.utils.DataBaseAcces;
import service.RestaurantService;

import java.io.IOException;
import java.sql.SQLException;

public class App extends Application {
    private DataBaseAcces data;
    private AbstractDataBaseRepo<String, MenuItem> menuItemRepo;
    private AbstractDataBaseRepo<String, Table> tableRepo;
    private AbstractDataBaseRepo<String, Order> orderRepo;
    private AbstractDataBaseRepo<Pair<String, String>, OrderItem> orderItemRepo;
    public RestaurantService service;


    @Override
    public void start(Stage stage) throws Exception {
        RestaurantService service;
        ValidatorFactory validatorFactory = ValidatorFactory.getInstance();
        IValidator menuItemValidator = validatorFactory.createValidator(EValidator.MENU_ITEM);
        IValidator tableValidator = validatorFactory.createValidator(EValidator.TABLE);
        IValidator orderValidator = validatorFactory.createValidator(EValidator.ORDER);
        IValidator orderItemValidator = validatorFactory.createValidator(EValidator.ORDER_ITEM);


        String url = "jdbc:postgresql://localhost:5432/restaurant";
        String username = "postgres";
        String password = "ivona2004";

        this.data = new DataBaseAcces(url, username, password);
        try {
            data.createConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DataBaseRepoFactory repoFactory = new DataBaseRepoFactory(data);
        this.menuItemRepo = repoFactory.createRepo(EDataBaseStrategy.MENU_ITEM, menuItemValidator);
        this.tableRepo = repoFactory.createRepo(EDataBaseStrategy.TABLE, tableValidator);
        this.orderRepo = repoFactory.createRepo(EDataBaseStrategy.ORDER, orderValidator);
        this.orderItemRepo = repoFactory.createRepo(EDataBaseStrategy.ORDER_ITEM, orderItemValidator);
        this.service = new RestaurantService(menuItemRepo, tableRepo, orderRepo, orderItemRepo);
        initView(stage);
        stage.setMinHeight(400);
        stage.setMinWidth(600);
        stage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader stageLoader = new FXMLLoader();
        stageLoader.setLocation(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(stageLoader.load());
        primaryStage.setTitle("Restaurant");
        primaryStage.setScene(scene);

        AngajatiController controller = stageLoader.getController();
        controller.setAngajatiController(service, primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
