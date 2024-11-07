package application.javafx1.controller;

import application.javafx1.Main;
import application.javafx1.guiUtil.Alerts;
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


 public class MainViewController implements Initializable {

     @FXML
     private MenuItem menuItemSeller;

     @FXML
     private MenuItem menuItemDepartment;

     @FXML
     private MenuItem menuItemAbout;

     @FXML
     public void onMenuItemSellerAction(){

         Alerts.showAlert("Titulo", null, "Click no menu Seller", Alert.AlertType.INFORMATION);

     }

     @FXML
     public void onMenuItemDepartment(){

         Alerts.showAlert("Titulo", null, "Click no menu Titulo", Alert.AlertType.INFORMATION);

     }

     @FXML
     public void onMenuItemAbout(){

         //Alerts.showAlert("Titulo", null, "Click no menu About", Alert.AlertType.INFORMATION);

         loadView("/application/javafx1/gui/About.fxml");

     }

     @Override
     public void initialize(URL location, ResourceBundle resources) {

     }

     private synchronized void loadView(String absoluteName){

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

         } catch (IOException e) {

             Alerts.showAlert("IOException", "Error Loader View", e.getMessage(), Alert.AlertType.ERROR);

         }


     }
 }
