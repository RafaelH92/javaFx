package application.javafx1.controller;

import application.javafx1.Main;
import application.javafx1.guiUtil.Alerts;
import application.javafx1.modelServices.DepartmentService;
import application.javafx1.modelServices.SellerService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;


public class MainViewController implements Initializable {

     @FXML
     private MenuItem menuItemSeller;

     @FXML
     private MenuItem menuItemDepartment;

     @FXML
     private MenuItem menuItemAbout;

     @FXML
     public void onMenuItemSellerAction(){

//         Alerts.showAlert("Titulo", null, "Click no menu Seller", Alert.AlertType.INFORMATION);
         loadView("/application/javafx1/gui/SellerList.fxml", (SellerListController controler) -> {
             controler.setSellerService(new SellerService());
             controler.updateTableView();
         });

     }

     @FXML
     public void onMenuItemDepartmentAction(){

         //Alerts.showAlert("Titulo", null, "Click no menu Titulo", Alert.AlertType.INFORMATION);
         loadView("/application/javafx1/gui/DepartmentList.fxml", (DepartmentListController controler) -> {
             controler.setDepartmentService(new DepartmentService());
             controler.updateTableView();
         });

     }

     @FXML
     public void onMenuItemAboutAction(){

         //Alerts.showAlert("Titulo", null, "Click no menu About", Alert.AlertType.INFORMATION);

         loadView("/application/javafx1/gui/About.fxml", x -> {});

     }

     @Override
     public void initialize(URL location, ResourceBundle resources) {

     }

     private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction){

         try {

             /* Manipulando a cena principal */

             FXMLLoader loader =  new FXMLLoader(getClass().getResource(absoluteName));
             VBox newVbox = loader.load();

             Scene mainScene = Main.getMainScene();
             VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

             Node mainMenu = mainVbox.getChildren().get(0);
             mainVbox.getChildren().clear();
             mainVbox.getChildren().add(mainMenu);
             mainVbox.getChildren().addAll(newVbox.getChildren());

             /* Atribui para uma variavel generica o controlador da tela instanciada */
             T controller = loader.getController();
             /* Executa a funcao generica passada como parametro */
             initializingAction.accept(controller);

         } catch (IOException e) {

             Alerts.showAlert("IOException", "Error Loader View", e.getMessage(), Alert.AlertType.ERROR);

         }


     }

 }
