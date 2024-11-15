package application.javafx1;

import application.javafx1.modelDao.DaoFactory;
import application.javafx1.modelDao.DepartmentDao;
import application.javafx1.modelDao.SellerDao;
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

        /* Teste de consulta */

        System.out.println("**** TEST 1: seller findById ****");
        SellerDao sellerdao = DaoFactory.createSellerDao();
        Seller seller = sellerdao.findById(3);

        System.out.println(seller);

        System.out.println("\n**** TEST 2: seller findByDepartment ****");
        Department dep = new Department(2, null);
        List<Seller> list = sellerdao.findByDepartment(dep);

        for (Seller obj : list){
            System.out.println(obj);
        }

        System.out.println("\n**** TEST 3: seller findAll ****");

        List<Seller> listFindAll = sellerdao.findAll();

        for (Seller obj : listFindAll){

            System.out.println(obj);
        }
        /*
        System.out.println("\n**** TEST 4: insert ****");

        Seller newSaller = new Seller(null, "Greg", "greg@gmail.com", 4000.0, new Date(), dep);

        sellerdao.insert(newSaller);

        System.out.println("Inserted! New id => " + newSaller.getId());

         */
        /*
        System.out.println("\n**** TEST 5: update ****");

        Seller selerUpdate = sellerdao.findById(8);

        selerUpdate.setName("Rafa");
        selerUpdate.setEmail("emailteste@gmail.com");
        selerUpdate.setBaseSalary(5000.0);
        sellerdao.update(selerUpdate);
        System.out.println("Update completed");



        System.out.println("\n**** TEST 6: delete ****");

        sellerdao.deleteById(8);
        System.out.println("Delete completed!");

        */

        launch();
    }
}