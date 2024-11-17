package application.javafx1;

import application.javafx1.modelDao.DaoFactory;
import application.javafx1.modelDao.DepartmentDao;
import application.javafx1.modelDao.SellerDao;
import application.javafx1.modelDaoImp.SellerDaoJDBC;
import application.javafx1.modelEntities.Department;
import application.javafx1.modelEntities.Seller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main extends Application {

    private static Scene mainScene;

    @Override
    public void start(Stage primaryStage) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/javafx1/gui/MainView.fxml"));

            ScrollPane scrollPane = loader.load();
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);

            mainScene = new Scene(scrollPane);

            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Sample JavaFX application");
            primaryStage.show();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public static Scene getMainScene(){

        return mainScene;

    }

    public static void main(String[] args) {

        launch();
    }
}