package org.example.restaurante;

import controller.AngajatiController;
import controller.MenuController;
import domain.*;
import domain.validator.EValidator;
import domain.validator.IValidator;
import domain.validator.ValidatorFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import repo.IMenuItemRepo;
import repo.database.factory.DataBaseRepoFactory;
import repo.database.factory.EDataBaseStrategy;
import repo.database.utils.AbstractDataBaseRepo;
import repo.database.utils.DataBaseAcces;
import service.RestaurantService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.StreamSupport;

public class App extends Application {
    private DataBaseAcces data;
    private AbstractDataBaseRepo<String, MenuItem> menuItemRepo;
    private AbstractDataBaseRepo<String, Table> tableRepo;
    private AbstractDataBaseRepo<String, Order> orderRepo;
    public RestaurantService service;


    @Override
    public void start(Stage stage) throws Exception {
        RestaurantService service;
        ValidatorFactory validatorFactory = ValidatorFactory.getInstance();
        IValidator menuItemValidator = validatorFactory.createValidator(EValidator.MENU_ITEM);
        IValidator tableValidator = validatorFactory.createValidator(EValidator.TABLE);
        IValidator orderValidator = validatorFactory.createValidator(EValidator.ORDER);


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
        this.menuItemRepo = repoFactory.createRepo(EDataBaseStrategy.menu_item, menuItemValidator);
        this.tableRepo = repoFactory.createRepo(EDataBaseStrategy.restaurant_table, tableValidator);
        this.orderRepo = repoFactory.createRepo(EDataBaseStrategy.restaurant_order, orderValidator);
        this.service = new RestaurantService(tableRepo, orderRepo, (IMenuItemRepo) menuItemRepo);
        initView(stage);
        stage.setMinHeight(400);
        stage.setMinWidth(600);
        stage.show();

        Iterable<Table> tables = this.service.getTables();
        StreamSupport.stream(tables.spliterator(), false).forEach(table->openWindow(table.getId()));
    }

    private void openWindow(String windowTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/restaurante/views/menu-view.fxml"));
            AnchorPane root = loader.load();

            // Get the controller and set data
            MenuController controller = loader.getController();
            controller.setService(service);
            controller.setLabelText(windowTitle);

            // Create a new stage (window)
            Stage stage = new Stage();
            stage.setTitle("Table");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
