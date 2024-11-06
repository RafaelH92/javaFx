package application.javafx1.controller;

import application.javafx1.guiUtil.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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

         Alerts.showAlert("Titulo", null, "Click no menu About", Alert.AlertType.INFORMATION);

     }

     @Override
     public void initialize(URL location, ResourceBundle resources) {

     }
 }
