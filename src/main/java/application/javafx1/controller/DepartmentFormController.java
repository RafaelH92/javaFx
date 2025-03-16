package application.javafx1.controller;

import application.javafx1.guiUtil.Alerts;
import application.javafx1.guiUtil.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private Label labelErrorName;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    @FXML
    public void onBtSaveAction(){
        Alerts.showAlert("Titulo", null, "Click no onBtSaveAction", Alert.AlertType.INFORMATION);
    }

    @FXML
    public void onBtCancelAction(){
        Alerts.showAlert("Titulo", null, "Click no onBtCancelAction", Alert.AlertType.INFORMATION);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

    /* Iniciar componentes da tela de cadastro */
    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }
}
